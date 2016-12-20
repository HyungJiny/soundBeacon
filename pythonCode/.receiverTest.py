from matplotlib.mlab import find
import pyaudio
import numpy as np
import math
import time

chunk = 1024
FORMAT = pyaudio.paInt16
CHANNELS = 1
RATE = 44100

signal_gap = 1

RECORD_SECONDS = 10
isPlayed = False
isNextbit = False
bitcount = 6
finalCode = 0
prevFreq = 0
pingCount = 2

def _streamTofrequency(stream):
	"""
	stream read data
	this data return frequency
	"""
	global chunk
	sound_data = stream.read(chunk)
	return _signalTofrequency(sound_data)

def _signalTofrequency(signal):
	"""
	change to frequence from receive MIC data
	"""
	signal = np.fromstring(signal, 'Int16')
	crossing = [math.copysign(1.0, s) for s in signal]
	index = find(np.diff(crossing))
	f0 = round(len(index) * RATE / (2*np.prod(len(signal))))
	return f0

def catchStartSignal(stream):
	"""
	if catch start signal 19Khz return True
	"""
	global chunk
	counter = 0
	allow = 19

	while(True):
		frequency = _streamTofrequency(stream)
		print(frequency)
		if frequency >= 18900 and frequency <= 19000:
			counter = _catchStartSignal(frequency, counter)
		if counter >= allow and counter <= durationTime(0.5):
			return True
			break

	return False

def _catchStartSignal(freq, count):
	if freq >=18900 and freq <= 19000:
		return count+1

def checkSignal(frequency):
	"""
	If sound signal is 18.5Khz then signal means 0
	else if it is 19.5Khz then means 1.
	"""
	global signal_gap

	if frequency >= 18400 and frequency <= 18500:
		time.sleep(signal_gap)
		return 0
	elif frequency >= 19400 and frequency <= 19500:
		time.sleep(signal_gap)
		return 1
	else:
		return -1

def durationTime(second):
	"""
	calcurate during time
	"""
	global RATE, chunck
	return round(RATE / chunk * second);

def receiveSignal(stream):
	"""
	receive sound signal and save bit
	return bit list
	"""
	signal_list = list()
	isSignalEnd = False
	while not isSignalEnd:
		frequency = _streamTofrequency(stream)
		signal = checkSignal(frequency)
		if signal < 0:
			isSignalEnd = True
		else:
			signal_list.append(signal)
	return signal_list

def asciiToString(ascii_list):
	"""
	Ascii bit data change to String list
	"""
	bit_index = 6
	charic = 0
	string_list = list()

	for bit in ascii_list:
		if bit == 1:
			charic += pow(2, bit_index)
		bit_index -= 1

		if bit_index < 0:
			bit_index = 6
			string_list.append(chr(charic))
			charic = 0

	return string_list

def main():
	global FORMAT, CHANNELS, RATE, chunk

	audio = pyaudio.PyAudio()
	stream = audio.open(format=FORMAT, channels=CHANNELS, rate=RATE, input=True, output=True, frames_per_buffer=chunk)

	if catchStartSignal(stream):
		signal_list = receiveSignal(stream)
		result = asciiToString(signal_list)

	print(result)

if __name__ == "__main__" :
	audio = pyaudio.PyAudio()
	stream = audio.open(format=FORMAT, channels=CHANNELS, rate=RATE, input=True, output=True, frames_per_buffer=chunk)

	#start_time = time.time()
	print(durationTime(RECORD_SECONDS))

	for i in range(0, durationTime(RECORD_SECONDS)):
		data = stream.read(chunk)
		frequency = _signalTofrequency(data)

		if frequency >= 18000:
			isPlayed = True
			print(i, "%.2f Frequency" %frequency)



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
				print('bit position : ', bitcount)
				print('analysed bit : ', currentBit)
				bitcount -= 1
				pingCount = 2

		if bitcount<0:
			isPlayed = False
			break
	#end_time = time.time()
	#execution_time = start_time - end_time

	print(finalCode)
	print(chr(finalCode))
	#print(execution_time)

"""
if __name__ == "__main__" :
	main()
"""
