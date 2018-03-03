package q;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;

import nn.NeuralNet;
import nn.NeuronType;

public class DoubleNetIO
{
	private static final String	DATA_DIR  = "data";
	private static final String	DELIMITER = "\t";

	public static DoubleNet loadFromFile(final File p_file)
	{
		BufferedReader reader = null;
		try
		{
			final DoubleNet ret = new DoubleNet();

			reader = new BufferedReader(new FileReader(p_file));
			final String line = reader.readLine();

			final String[] bits = line.split(DELIMITER);
			final int numInputs = Integer.parseInt(bits[0]);
			final int numHidden = Integer.parseInt(bits[1]);
			final int numOutputs = Integer.parseInt(bits[2]);

			ret.setNetA(new NeuralNet(numInputs, numHidden, numOutputs, true));
			ret.setNetB(new NeuralNet(numInputs, numHidden, numOutputs, true));

			// read A from file
			for(int hiddenIndex = 0; hiddenIndex < numHidden; hiddenIndex++)
			{
				readHiddenWeightsLine(reader, ret.getNetA(), numInputs, hiddenIndex);
			}

			for(int outputIndex = 0; outputIndex < numOutputs; outputIndex++)
			{
				readOutputWeightsLine(reader, ret.getNetA(), numHidden, outputIndex);
			}

			// read B from file
			for(int hiddenIndex = 0; hiddenIndex < numHidden; hiddenIndex++)
			{
				readHiddenWeightsLine(reader, ret.getNetB(), numInputs, hiddenIndex);
			}

			for(int outputIndex = 0; outputIndex < numOutputs; outputIndex++)
			{
				readOutputWeightsLine(reader, ret.getNetB(), numHidden, outputIndex);
			}

			ret.reassignNets();
			return ret;
		}
		catch(final IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				reader.close();
			}
			catch(final IOException e)
			{
				e.printStackTrace();
			}
		}

		return null;
	}

	private static void readHiddenWeightsLine(final BufferedReader p_reader, final NeuralNet p_net,
			final int p_numInputs, final int p_hiddenIndex) throws IOException
	{
		final int numExpected = p_numInputs + 1;

		final String[] bits = p_reader.readLine().split(DELIMITER);
		if(bits.length != numExpected)
		{
			throw new RuntimeException();
		}

		for(int inputIndex = 0; inputIndex < p_numInputs; inputIndex++)
		{
			final double weight = Double.parseDouble(bits[inputIndex]);
			p_net.setWeight(NeuronType.HIDDEN, p_hiddenIndex, inputIndex, weight);
		}
		final double bias = Double.parseDouble(bits[p_numInputs]);
		p_net.setBias(NeuronType.HIDDEN, p_hiddenIndex, bias);
	}

	private static void readOutputWeightsLine(final BufferedReader p_reader, final NeuralNet p_net,
			final int p_numHidden, final int p_outputIndex) throws IOException
	{
		final int numExpected = p_numHidden + 1;

		final String[] bits = p_reader.readLine().split(DELIMITER);
		if(bits.length != numExpected)
		{
			throw new RuntimeException();
		}

		for(int hiddenIndex = 0; hiddenIndex < p_numHidden; hiddenIndex++)
		{
			final double weight = Double.parseDouble(bits[hiddenIndex]);
			p_net.setWeight(NeuronType.OUTPUT, p_outputIndex, hiddenIndex, weight);
		}
		final double bias = Double.parseDouble(bits[p_numHidden]);
		p_net.setBias(NeuronType.OUTPUT, p_outputIndex, bias);
	}

	public static void saveToFile(final DoubleNet p_net, final File p_file)
	{
		BufferedWriter writer = null;
		try
		{
			writer = new BufferedWriter(new FileWriter(p_file));
			final String out = p_net.numInputs() + DELIMITER + p_net.numHidden() + DELIMITER + p_net.numOutputs();
			writeLine(writer, out);

			writeHiddenLines(writer, p_net.getNetA());
			writeOutputLines(writer, p_net.getNetA());

			writeHiddenLines(writer, p_net.getNetB());
			writeOutputLines(writer, p_net.getNetB());
		}
		catch(final IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(writer != null)
				{
					writer.close();
				}
			}
			catch(final IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static DoubleNet selectAndLoadFromFile()
	{
		final JFileChooser chooser = new JFileChooser(DATA_DIR);
		final int returnVal = chooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			final File selected = chooser.getSelectedFile();
			return loadFromFile(selected);
		}

		return null;
	}

	public static File selectFileAndSave(final DoubleNet p_net)
	{
		final JFileChooser chooser = new JFileChooser(DATA_DIR);
		final int returnVal = chooser.showSaveDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			final File selected = chooser.getSelectedFile();
			saveToFile(p_net, selected);
			return selected;
		}

		return null;
	}

	private static void writeHiddenLines(final BufferedWriter writer, final NeuralNet p_net) throws IOException
	{
		for(int hiddenIndex = 0; hiddenIndex < p_net.numHidden(); hiddenIndex++)
		{
			String out = "";
			for(int inputIndex = 0; inputIndex < p_net.numInputs(); inputIndex++)
			{
				out += p_net.getWeight(NeuronType.HIDDEN, hiddenIndex, inputIndex) + DELIMITER;
			}
			out += p_net.getBias(NeuronType.HIDDEN, hiddenIndex);
			writeLine(writer, out);
		}
	}

	private static void writeLine(final BufferedWriter p_writer, final String p_out) throws IOException
	{
		p_writer.write(p_out);
		p_writer.newLine();
	}

	private static void writeOutputLines(final BufferedWriter p_writer, final NeuralNet p_net) throws IOException
	{
		for(int outputIndex = 0; outputIndex < p_net.numOutputs(); outputIndex++)
		{
			String out = "";
			for(int hiddenIndex = 0; hiddenIndex < p_net.numHidden(); hiddenIndex++)
			{
				out += p_net.getWeight(NeuronType.OUTPUT, outputIndex, hiddenIndex) + DELIMITER;
			}
			out += p_net.getBias(NeuronType.OUTPUT, outputIndex);
			writeLine(p_writer, out);
		}
	}
}
