import struct
import wave

import matplotlib.animation as animation
import matplotlib.pyplot as plt
import numpy as np
import pyaudio

SAVE = 0.0
TITLE = ''
WIDTH = 1280
HEIGHT = 720
FPS = 25.0

nFFT = 512
BUF_SIZE = 4*nFFT
FORMAT = pyaudio.paInt16
CHANNELS = 2
RATE = 44100

def animate(i, line, stream, wf, MAX_y):
	N = max(stream.get_read_available() / nFFT, 1) * nFFT
	data = stream.read(N)
	if SAVE:
		wf.writeframes(data)

	y = np.array(struct.unpack("%dh" % (N * CHANNELS), data)) / MAX_y
	y_L = y[::2]
	y_R = y[1::2]

	Y_L = np.fft.fft(y_L, nFFT)
	Y_R = np.fft.fft(y_R, nFFT)

	Y = abs(np.hstack((Y_L[-nFFT / 2:-1], Y_R[:nFFT / 2])))

	line.set_ydata(Y)
	return line,

def init(line):
	line.set_ydata(np.zeros(mFFT - 1))
	return line,

def main():
	dpi = plt.rcParams['figure.dpi']
	plt.rcParams['savefig.dpi'] = dpi
	plt.rcParams["figure.figsize"] = (1.0 * WIDTH / dpi, 1.0 * HEIGHT / dpi)

	fig = plt.figure()

	x_f = 1.0 * np.arange(-nFFT / 2 + 1, nFFT / 2) / nFFT * RATE

