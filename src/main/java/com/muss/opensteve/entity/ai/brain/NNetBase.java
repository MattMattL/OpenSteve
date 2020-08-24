package com.muss.opensteve.entity.ai.brain;

public class NNetBase
{
	public final int NET_IN;
	public final int NET_MID;
	public final int NET_OUT;

	public double inputVector[]; // input vector
	protected double outputVector[]; // equivalent to outputL2
	public double desiredOut[]; // desired results

	protected double weightsL1[][];
	protected double weightsL2[][];

	protected double productL1[];
	protected double productL2[];
	protected double outputL1[];

	protected double delta[];

	public NNetBase(int netIn, int netMid, int netOut)
	{
		NET_IN = netIn;
		NET_MID = netMid;
		NET_OUT = netOut;

		this.inputVector = new double[NET_IN];
		this.desiredOut = new double[NET_OUT];

		this.weightsL1 = new double[NET_IN][NET_MID];
		this.weightsL2 = new double[NET_MID][NET_OUT];

		this.productL1 = new double[NET_MID];
		this.productL2 = new double[NET_OUT];
		this.outputL1 = new double[NET_MID];
		this.outputVector = new double[NET_OUT];

		this.delta = new double[NET_OUT];

		initialiseWeights();
		initialiseVectors();
	}

	private void initialiseWeights()
	{
		int i, j, k;

		for(i=0; i<NET_IN; i++)
			for(j=0; j<NET_MID; j++)
				weightsL1[i][j] = (double)(Math.random() * 2) - 1; // [-1, 1]

		for(j=0; j<NET_MID; j++)
			for(k=0; k<NET_OUT; k++)
				weightsL2[j][k] = (double)(Math.random() * 2) - 1; // [-1, 1]
	}

	private void initialiseVectors()
	{
		int i, k;

		// initialise inputVector vectors
		for(i=0; i<NET_IN; i++)
		{
			inputVector[i] = (int)(Math.random()*2);
			inputVector[i] %= 2;
		}

		// initialise output vectors
		this.nnRunFeedforward();

		// initialise desiredOut output vectors
		for(k=0; k<NET_OUT; k++)
		{
			desiredOut[k] = (int)(Math.random()*2);
			desiredOut[k] %= 2;
		}
	}

	public void nnRunFeedforward()
	{
		int i, j, k;

		for(j=0; j<NET_MID; j++)
		{
			productL1[j] = 0.0;

			for(i=0; i<NET_IN; i++)
				productL1[j] += inputVector[i] * weightsL1[i][j];

			outputL1[j] = 1.0/(1 + Math.exp(productL1[j]));
		}

		for(k=0; k<NET_OUT; k++)
		{
			productL2[k] = 0.0;

			for(j=0; j<NET_MID; j++)
				productL2[k] += outputL1[j] * weightsL2[j][k];

			outputVector[k] = 1.0/(1 + Math.exp(productL2[k]));
		}
	}

	public void nnRunBackprop()
	{
		int i, j, k;

		for(k=0; k<NET_OUT; k++)
		{
			delta[k] = (desiredOut[k]-outputVector[k]) * (outputVector[k]*(1-outputVector[k]));

			for(j=0; j<NET_MID; j++)
			{
				weightsL2[j][k] += delta[k] * outputL1[j];

				for(i=0; i<NET_IN; i++)
				{
					weightsL1[i][j] += delta[k] * weightsL2[j][k] * (outputL1[j]*(1-outputL1[j])) * inputVector[i];
				}
			}
		}
	}

	public int nnGetMaxOutputNode()
	{
		int iMax = 0;

		for(int k=1; k<NET_OUT; k++)
		{
			if(outputVector[k] > outputVector[iMax])
				iMax = k;
		}

		return iMax;
	}
}

