from __future__ import division
from numpy.fft import rfft
from numpy import argmax, mean, diff, log
from matplotlib.mlab import find
from scipy.signal import blackmanharris, fftconvolve
from time import time
import sys
try:
	import soundfile as sf
except ImportError:
	from scikits.audiolab import flacread

def freq_from_crossings(sig, fs):
	indices = find((sig[1:] >= 0) & (sig[:-1] < 0))
	crossings = [i - sig[i] / (sig[i+1] - sig[i]) for i in indices]
	return fs / mean(diff(crossings))

def freq_from_fft(sig, fs):
	windowed = sig * blackmanharris(len(sig))
	f = rfft(windowed)

	i = argmax(abs(f))
	true_i = parabolic(log(abs(f)), i)[0]

	return fs * true_i / len(windowed)

def freq_from_autocorr(sig, fs):
	corr = fftconvolve(sig, sig[::-1], mode='full')
	corr = corr[len(corr) // 2:]

	d = diff(corr)
	start = find(d > 0)[0]

	peak = argmax(corr[start:]) + start
	px, py = parabolic(corr, peak)

	return fs / px

def freq_from_HPS(sig, fs):
	windowed = sig * blackmanharris(len(sig))

	from pylab import subplot, plot, log, copy, show
	c = abs(rfft(windowed))
	maxharms = 8
	subplot(maxharms, 1, 1)
	plot(log(c))
	for x in range(2, maxharms):
		a = copy(c[::x])
		c = c[:len(a)]
		i = argmax(abs(c))
		true_i = parabolic(abs(c), i)[0]
		print 'Pass %d: %f Hz' %(x, fs * true_i / len(windowed))
		c *= a
		subplot(maxharms, 1, x)
		plot(log(c))
	show()

filename = sys.argv[1]

print 'Reading file "%s" ' %filename

try:
	signal, fs = sf.read(filename)
except NameError:
	singal, fs, enc = flacread(filename)

print 'Calculating frequency from FFT:',
start_time = time()
print '%f Hz' %freq_from_fft(signal, fs)
print 'Time elapsed: %.3f s\n' % (time() - start_time)

print 'Calculating frequency from zero crossings:', 
start_time=time()
print '%f Hz' %freq_from_crossings(signal, fs)
print 'Time elapsed: %.3f s\n' % (time() - start_time)

print 'Calculating frequency from autocorrelation:', 
start_time=time()
print '%f Hz' % freq_from_autocorr(signal, fs)
print 'Time elapsed: %.3f s\n' % (time() - start_time)

print 'Calculating frequency from harmonic product spectrum:', 
start_time=time()
#print '%f Hz' % freq_from_autocorr(signal, fs)
print 'Time elapsed: %.3f s\n' % (time() - start_time)

