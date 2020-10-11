package com.muss.opensteve.entity.ai.brain;

public class BackPropLog
{
	private final int length;
	private double inputVector[][];
	private double outputVector[][];

	private int offset;

	public BackPropLog(int lengthIn, int netIn, int netOut)
	{
		this.inputVector = new double[lengthIn][netIn];
		this.outputVector = new double[lengthIn][netOut];

		this.length = lengthIn;
		this.offset = 0;
	}

	public void tick()
	{
		this.offset = (++this.offset < length) ? this.offset : this.offset % this.length;
	}

	public void copy(double nnetInput[], double nnetOutput[])
	{
		for(int i=0; i<nnetInput.length; i++)
			this.inputVector[this.offset][i] = nnetInput[i];

		for(int i=0; i<nnetOutput.length; i++)
			this.outputVector[this.offset][i] = nnetOutput[i];
	}

	public double[] getInputVec()
	{
		return this.inputVector[this.offset];
	}

	public double[] getOutputVec()
	{
		return this.outputVector[this.offset];
	}
}
