package com.muss.opensteve.entity.ai.brain;

public class NNetBase
{
	public final int NET_IN;
	public final int NET_MID;
	public final int NET_OUT;

	protected double input[];	// input vector
	protected double desired[];	// desired results

	protected double wL1[][];
	protected double wL2[][];

	protected double pL1[];
	protected double pL2[];
	protected double y[];
	protected double z[];		// output vector

	protected double delta[];

	protected NNetBase(int netIn, int netMid, int netOut)
	{
		// set network size
		NET_IN = netIn;
		NET_MID = netMid;
		NET_OUT = netOut;

		// allocate memory
		this.input = new double[NET_IN];
		this.desired = new double[NET_OUT];

		this.wL1 = new double[NET_IN][NET_MID];
		this.wL2 = new double[NET_MID][NET_OUT];

		this.pL1 = new double[NET_MID];
		this.pL2 = new double[NET_OUT];
		this.y = new double[NET_MID];
		this.z = new double[NET_OUT];

		this.delta = new double[NET_OUT];

		// initialise
		initialiseWeights();
		initialiseVectors();
	}

	private void initialiseWeights()
	{
		int i, j, k;

		for(i=0; i<NET_IN; i++)
			for(j=0; j<NET_MID; j++)
				wL1[i][j] = (double)(Math.random() * 2) - 1; // [-1, 1]

		for(j=0; j<NET_MID; j++)
			for(k=0; k<NET_OUT; k++)
				wL2[j][k] = (double)(Math.random() * 2) - 1; // [-1, 1]
	}

	private void initialiseVectors()
	{
		int i, k;

		// initialise input vectors
		for(i=0; i<NET_IN; i++)
		{
			input[i] = (int)(Math.random()*2);
			input[i] %= 2;
		}

		// initialise output vectors
		this.nnRunFeedforward();

		// initialise desired output vectors
		for(k=0; k<NET_OUT; k++)
		{
			desired[k] = (int)(Math.random()*2);
			desired[k] %= 2;
		}
	}

	@Deprecated
	private double sigmoid(double x)
	{
		return 1.0/(1 + Math.exp(-x));
	}

	public void nnRunFeedforward()
	{
		int i, j, k;

		for(j=0; j<NET_MID; j++)
		{
			pL1[j] = 0.0;

			for(i=0; i<NET_IN; i++)
				pL1[j] += input[i] * wL1[i][j];

			y[j] = 1.0/(1 + Math.exp(pL1[j]));
		}

		for(k=0; k<NET_OUT; k++)
		{
			pL2[k] = 0.0;

			for(j=0; j<NET_MID; j++)
				pL2[k] += y[j] * wL2[j][k];

			z[k] = 1.0/(1 + Math.exp(pL2[k]));
		}
	}

	public void nnRunBackprop()
	{
		int i, j, k;

		for(k=0; k<NET_OUT; k++)
		{
			delta[k] = (desired[k]-z[k]) * (z[k]*(1-z[k]));

			for(j=0; j<NET_MID; j++)
			{
				wL2[j][k] += delta[k] * y[j];

				for(i=0; i<NET_IN; i++)
				{
					wL1[i][j] += delta[k] * wL2[j][k] * (y[j]*(1-y[j])) * input[i];
				}
			}
		}
	}
}

