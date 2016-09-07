from matplotlib.mlab import find
import pyaudio
import numpy as np
import math

chunk = 1024
FORMAT = pyaudio.paInt16
CHANNELS = 1
RATE = 44100
RECORD_SECONDS = 10

isPlayed = False
isNextbit = False
bitcount = 6
finalCode = 0

prevFreq = 0
pingCount = 2

def Pitch(signal):
	signal = np.fromstring(signal, 'Int16')
	crossing = [math.copysign(1.0, s) for s in signal]
	index = find(np.diff(crossing))
	f0 = round(len(index) * RATE / (2*np.prod(len(signal))))	
	return f0;


if __name__ == '__main__':
	p = pyaudio.PyAudio()

	stream = p.open(format=FORMAT, channels=CHANNELS, rate=RATE, input=True, output=True, frames_per_buffer=chunk)

	for i in range(0, RATE / chunk * RECORD_SECONDS):
		data = stream.read(chunk)
		frequency = Pitch(data)
	
		#print i
		if frequency >= 18000:
			isPlayed = True
			#print "%.2f Frequency" %frequency

		if isPlayed and bitcount>=0:
			if frequency >= 18400 and frequency <= 18550 and not isNextbit:
				isNextbit = True
				finalCode += 0
				currentBit = 0
				prevFreq = frequency
			elif frequency >= 19400 and frequency <= 19550 and not isNextbit:
				isNextbit = True
				finalCode += pow(2, bitcount)
				currentBit = 1
				prevFreq = frequency
		
		dif = prevFreq - frequency
		#if isPlayed and dif >= 50 and frequency <= 18300 and frequency >= 18000:
		if isPlayed and dif >= 200:
			pingCount -= 1
			if pingCount < 0:
				isNextbit = False
				print 'present bit : ', bitcount
				print 'current bit : ', currentBit
				bitcount -= 1
				pingCount = 2
		
		if bitcount<0:
			isPlayed = False
			break
	
	print finalCode
	print chr(finalCode)

