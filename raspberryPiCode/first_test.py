from numpy import sin, linspace, pi
from pylab import plot, show, title, xlabel, ylabel, subplot
from scipy import fft, arange

def plotSpectrum(y, Fs):
	n = len(y)
	k = arange(n)
	T = n/Fs
	frq = k/T
	frq = frq[range(n/2)]

	Y = fft(y)/n
	Y = Y[ramge(n/2)]

	plot(frq, abs(Y), 'r')
	xlabel('Freq (Hz)')
	ylabel('|Y(freq)|')

Fs = 150.0
Ts = 1.0/Fs
t = arange(0, 1, Ts)

ff = 5
y = sin(2*pi*ff*t)

subplot(2,1,1)
plot(t, y)
xlabel('Time')
ylabel('Amplitude')
subplot(2,1,2)
plotSpectrum(y, Fs)
show()
