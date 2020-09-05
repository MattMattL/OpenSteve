package com.muss.opensteve.entity.ai.brain;

import java.util.Random;

public class DeepNNetBase
{
	private final int NET_DEPTH;
	private final int NET_MAX_WIDTH;
	private final int NET_IN;
	private final int NET_OUT;

	protected final int NET_WIDTH[];

	public double vectorIn[];
	public double vectorOut[];
	public double desiredOut[];

	protected double weights[][][];
	private double products[][];
	private double sigmoid[][];
	private double delta[][];

	public Random random = new Random(System.currentTimeMillis());

	public DeepNNetBase(int netIn, int netOut, int netDepth)
	{
		this.NET_IN = netIn;
		this.NET_OUT = netOut;
		this.NET_DEPTH = netDepth;
		this.NET_MAX_WIDTH = Math.max(this.NET_IN, this.NET_OUT);

		NET_WIDTH = new int[NET_DEPTH + 1];

		memoryInit();
		vectorInit();
	}

	private void memoryInit()
	{
		vectorIn = new double[NET_IN];
		vectorOut = new double[NET_OUT];
		desiredOut = new double[NET_OUT];

		weights = new double[NET_DEPTH][NET_MAX_WIDTH][NET_MAX_WIDTH];
		products = new double[NET_DEPTH][NET_MAX_WIDTH];
		sigmoid = new double[NET_DEPTH + 1][NET_MAX_WIDTH];

		delta = new double[NET_DEPTH][NET_MAX_WIDTH];

		NET_WIDTH[0] = NET_IN;
		NET_WIDTH[NET_DEPTH] = NET_OUT;

		for(int l=1; l<NET_DEPTH; l++)
			NET_WIDTH[l] = NET_MAX_WIDTH; // temporary initialisation method
	}

	private void vectorInit()
	{
		for(int i=0; i<NET_IN; i++)
			vectorIn[i] = random.nextDouble();

		for(int i=0; i<NET_OUT; i++)
			 desiredOut[i] = random.nextDouble();

		for(int l=0; l<NET_DEPTH; l++)
		{
			for(int i=0; i<NET_WIDTH[l]; i++)
				for(int j=0; j<NET_WIDTH[l+1]; j++)
					weights[l][i][j] = random.nextDouble() * 2 - 1;
		}
	}

	public void nnRunFeedForward()
	{
		int in, out, l, i, j;

		// copy input vector
		for(in=0; in<NET_IN; in++)
			sigmoid[0][in] = vectorIn[in];

		// run neural networks
		for(l=0; l<NET_DEPTH; l++)
		{
			for(j=0; j<NET_WIDTH[l+1]; j++)
			{
				products[l][j] = 0;

				for(i=0; i<NET_WIDTH[l]; i++)
					products[l][j] += sigmoid[l][i] * weights[l][i][j];
			}

			for(j=0; j<NET_WIDTH[l+1]; j++)
				sigmoid[l+1][j] = 1.0/(1 + Math.exp(-products[l][j]));
		}

		// save output vector
		for(out=0; out<NET_OUT; out++)
			vectorOut[out] = sigmoid[NET_DEPTH][out];
	}

	public void nnRunBackprop()
	{
		int layer, in, out, l, i, j, k;

		// calculate delta
		for(out=0; out<NET_OUT; out++)
			delta[NET_DEPTH-1][out] = 0;

		for(out=0; out<NET_OUT; out++)
			delta[NET_DEPTH-1][out] += (desiredOut[out] - vectorOut[out]) * vectorOut[out] * (1 - vectorOut[out]);

		for(layer=NET_DEPTH - 2; layer>=0 ; layer--)
		{
			for(j=0; j<NET_WIDTH[layer+1]; j++)
			{
				delta[layer][j] = 0;

				for(k=0; k<NET_WIDTH[layer+2]; k++)
					delta[layer][j] += delta[layer+1][k] * weights[layer+1][j][k] * sigmoid[layer+1][j] * (1 - sigmoid[layer+1][j]);
			}
		}
		// adjust the weights
		for(layer=0; layer<NET_DEPTH; layer++)
		{
			for(j=0; j<NET_WIDTH[layer+1]; j++)
				for(i=0; i<NET_WIDTH[layer]; i++)
					weights[layer][i][j] += delta[layer][j] * sigmoid[layer][i];
		}
	}
}

