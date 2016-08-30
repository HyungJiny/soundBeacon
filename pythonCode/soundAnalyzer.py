import sys
import alsaaudio
from scipy import arange, fft, fromstring, roll, zeros

import pyqtgraph as pg
from pyqtgraph.Qt import QtGui, QtCore

fftLen = 2048
shift = 100
signal_scale = 1. / 2000

capture_setting = {
	"ch" : 1,
	"fs" : 16000,
	"chunk" : shift
}

def spectrumAnalyzer():
	global fftLen, capture_setting, signal_scale
	##########################
	# Capture Sound from Mic #
	##########################
	ch = capture_setting["ch"]
	fs = capture_setting["fs"]
	chunk = capture_setting["chunk"]
	inPCM = alsaaudio.PCM(alsaaudio.PCM_CAPTURE)
	inPCM.setchannels(ch)
	inPCM.setrate(fs)
	inPCM.setformat(alsaaudio.PCM_FORMAT_S16_LE)
	inPCM.setperiodsize(chunk)

	signal = zeros(fftLen, dtype = float)

	##########
	# Layout #
	##########
	app = QtGui.QApplication([])
	app.quitOnLastWindowClosed()

	mainWindow = QtGui.QMainWindow()
	mainWindow.setWindowTitle("Spectrum Analyzer")
	mainWindow.resize(800, 300)

	centralWid = QtGui.QWidget()
	mainWindow.setCentralWidget(centralWid)

	lay = QtGui.QVBoxLayout()
	centralWid.setLayout(lay)

	specWid = pg.PlotWidget(name="spectrum")
	specItem = specWid.getPlotItem()
	specItem.setMouseEnabled(y = False)
	specItem.setYRange(0, 1000)
	specItem.setXRange(0, fftLen / 2, padding = 0)

	specAxis = specItem.getAxis("bottom")
	specAxis.setLabel("Frequency [Hz]")
	specAxis.setScale(fs / 2. / (fftLen / 2 + 1))
	hz_interval = 500
	newXAxis = (arange(int(fs / 2 / hz_interval)) + 1) * hz_interval
	oriXAxis = newXAxis / (fs / 2. / (fftLen / 2 + 1))
	specAxis.setTicks([zip(oriXAxis, newXAxis)])

	lay.addWidget(specWid)

	mainWindow.show()

	# update
	for time in range(100):
		length, data = inPCM.read()
		num_data = fromstring(data, dtype = "int16")
		signal = roll(signal, - chunk)
		signal[- chunk :] = num_data
		fftspec = fft(signal)

		print signal[1800:1900]
		specItem.plot(abs(fftspec[1 : fftLen / 2 + 1] * signal_scale), clear = True)
		QtGui.QApplication.processEvents()

if __name__ == "__main__":
	spectrumAnalyzer()
