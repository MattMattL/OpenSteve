package com.muss.opensteve.entity.ai.brain;

public class NNetIO extends NNetBase
{
	public NNetIO(int netIn, int netMid, int netOut)
	{
		super(netIn, netMid, netOut);
	}

	private void nnBarf(String method, String message)
	{
		System.out.printf("[error] NNetIO::%s %s\n", method, message);
	}


	// Initialising vectors and weights //

	public void nnSetInputVec(int pos, double vector)
	{
		if(pos >= this.NET_IN || pos < 0)
		{
			this.nnBarf("nnSetInputVec", "Invalid position requested (double)");
			return;
		}

		this.input[pos] = vector;
	}

	public void nnSetInputVec(int pos, int vector)
	{
		if(pos >= this.NET_IN || pos < 0)
		{
			this.nnBarf("nnSetInputVec", "Invalid position requested (int)");
			return;
		}

		this.input[pos] = vector;
	}


	public void nnSetDesiredOut(int pos, double vector)
	{
		if(pos >= this.NET_OUT || pos < 0)
		{
			this.nnBarf("nnSetDesiredOut", "Invalid position requested (double)");
			return;
		}

		this.desired[pos] = vector;
	}

	public void nnSetDesiredOut(int pos, int vector)
	{
		if(pos >= this.NET_OUT || pos < 0)
		{
			this.nnBarf("nnSetDesiredOut", "Invalid position requested (int)");
			return;
		}

		this.desired[pos] = vector;
	}

	public void nnSetWeightAt(int layerIn, int layerA, int layerB, double weightIn)
	{
		switch(layerIn)
		{
			case 1:
			{
				if(0<=layerA && layerA<NET_IN && 0<=layerB && layerB<NET_MID)
					this.wL1[layerA][layerB] = weightIn;

				return;
			}

			case 2:
			{
				if(0<=layerA && layerA<NET_MID && 0<=layerB && layerB<NET_OUT)
					this.wL2[layerA][layerB] = weightIn;

				return;
			}
		}

		this.nnBarf("nnSetWeightAt", "Address requested not initialised");
	}


	// Returning requested output vectors or values //

	public double nnGetInputVec(int pos)
	{
		if(pos >= this.NET_IN || pos < 0)
		{
			this.nnBarf("nnGetInputVec", "Invalid position requested");
			return 0.0;
		}

		return this.input[pos];
	}

	public double nnGetOutputVec(int pos)
	{
		if(pos >= this.NET_OUT || pos < 0)
		{
			this.nnBarf("nnGetOutputVec", "Invalid position requested");
			return 0.0;
		}

		return this.z[pos];
	}

	public double nnGetDesiredOut(int pos)
	{
		if(pos >= this.NET_OUT || pos < 0)
		{
			this.nnBarf("nnGetDesiredOut", "Invalid position requested");
			return 0.0;
		}

		return this.desired[pos];
	}

	public int nnGetMaxOutputNode()
	{
		int maxIndex = 0;

		for(int k=1; k<NET_OUT; k++)
		{
			if(nnGetOutputVec(k) > nnGetOutputVec(maxIndex))
				maxIndex = k;
		}

		return maxIndex;
	}

	public double nnGetWeightAt(int layerIn, int layerA, int layerB)
	{
		switch(layerIn)
		{
			case 1:
			{
				if(0<=layerA && layerA<NET_IN && 0<=layerB && layerB<NET_MID)
					return this.wL1[layerA][layerB];
			}

			case 2:
			{
				if(0<=layerA && layerA<NET_MID && 0<=layerB && layerB<NET_OUT)
					return this.wL2[layerA][layerB];
			}
		}

		this.nnBarf("nnGetWeightAt", "Address requested not initialised");
		return 0;
	}


	// TEST: Print network info to console //

	/*public void printNNOut()
	{
		System.out.printf("[Net]");

		int iteration = (NET_OUT > NET_IN)? NET_OUT : NET_IN;

		for(int i=0; i<iteration; i++)
		{
			if(i<NET_IN)
				System.out.printf("\t%8.2f", this.input[i]);
			else
				System.out.printf("\t        ");

			System.out.printf("  -->  ");

			if(i<NET_OUT)
				System.out.printf("%4.2f  (%3.1f)\n", this.z[i], this.desired[i]);
			else
				System.out.printf("\n");
		}

		System.out.printf("\n");
	}

	public void printWeightsOut()
	{
		int i, j, k;

		for(i=0; i<NET_IN; i++)
		{
			for(j=0; j<NET_MID; j++)
				System.out.printf("%6.2f ", wL1[i][j]);
			System.out.printf("\n");
		}
		System.out.printf("\n");

		for(i=0; i<NET_MID; i++)
		{
			for(j=0; j<NET_OUT; j++)
				System.out.printf("%6.2f ", wL2[i][j]);
			System.out.printf("\n");
		}
		System.out.printf("\n");
	}*/
}




