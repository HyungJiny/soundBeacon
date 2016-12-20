from matplotlib.mlab import find
import pyaudio
import numpy as np
import math
import time

chunk = 1024
FORMAT = pyaudio.paInt16
CHANNELS = 1
RATE = 44100

signal_gap = 0.1

def durationTime(second):
	"""
	calcurate during time
	"""
	global RATE, chunck
	return round(RATE / chunk * second)

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
	allow = 4

	during_time = durationTime(0.5)
	while(True):
	#for i in range(during_time):
		frequency = _streamTofrequency(stream)
		print(frequency)
		if checkSignal(frequency) == 2:
			counter += 1
			return True
	return False

def checkSignal(frequency):
	"""
	If sound signal is 18.5Khz then signal means 0
	else if it is 19.5Khz then means 1.
	"""
	global signal_gap

	if frequency >= 18400 and frequency <= 18500:
		return 0 # bit 0
	elif frequency >= 19400 and frequency <= 19500:
		return 1 # bit 1
	elif frequency >= 18900 and frequency <=19000:
		return 2 # bit start signal
	elif frequency <= 18200:
		return -2
	else:
		return -1

def receiveSignal(stream):
	"""
	receive sound signal and save bit
	return bit list
	"""
	signal_list = list()
	isnotSignalEnd = True
	end_count = 0
	isSameSignal = False
	prev_signal = -1

	while isnotSignalEnd:
		frequency = _streamTofrequency(stream)
		signal = checkSignal(frequency)
		print(signal, frequency)
		if signal == 1 or signal == 0:
			end_count = 0
			if isSameSignal and signal == prev_signal:
				print("same signal")
				continue
			else:
				signal_list.append(signal)
				isSameSignal = True
			prev_signal = signal
		elif signal == 2:
			continue
		elif signal == -2:
			end_count += 1
			if end_count >= 2:
				isSameSignal = False
			if end_count >= 20:
				isnotSignalEnd = False
		else:
			if end_count > 1:
				end_count = 0
				isSameSignal = False
			else:
				end_count += 1

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
	result = list()

	audio = pyaudio.PyAudio()
	stream = audio.open(format=FORMAT, channels=CHANNELS, rate=RATE, input=True, output=True, frames_per_buffer=chunk)

	if catchStartSignal(stream):
		signal_list = receiveSignal(stream)
		result = asciiToString(signal_list)

	print("\nSignal list : ", signal_list, len(signal_list))
	if len(result) <= 0:
		print("\nCan't receive Signal\n")
	else:
		print("\nResult : ",result)

if __name__ == "__main__" :
	main()
