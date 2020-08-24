package com.muss.opensteve.entity.ai.brain;

@Deprecated
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

		this.inputVector[pos] = vector;
	}

	public void nnSetInputVec(int pos, int vector)
	{
		if(pos >= this.NET_IN || pos < 0)
		{
			this.nnBarf("nnSetInputVec", "Invalid position requested (int)");
			return;
		}

		this.inputVector[pos] = vector;
	}

	public void nnSetDesiredOut(int pos, double vector)
	{
		if(pos >= this.NET_OUT || pos < 0)
		{
			this.nnBarf("nnSetDesiredOut", "Invalid position requested (double)");
			return;
		}

		this.desiredOut[pos] = vector;
	}

	public void nnSetDesiredOut(int pos, int vector)
	{
		if(pos >= this.NET_OUT || pos < 0)
		{
			this.nnBarf("nnSetDesiredOut", "Invalid position requested (int)");
			return;
		}

		this.desiredOut[pos] = vector;
	}

	public void nnSetWeightAt(int layerIn, int layerA, int layerB, double weightIn)
	{
		switch(layerIn)
		{
			case 1:
			{
				if(0<=layerA && layerA<NET_IN && 0<=layerB && layerB<NET_MID)
					this.weightsL1[layerA][layerB] = weightIn;

				return;
			}

			case 2:
			{
				if(0<=layerA && layerA<NET_MID && 0<=layerB && layerB<NET_OUT)
					this.weightsL2[layerA][layerB] = weightIn;

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

		return this.inputVector[pos];
	}

	public double nnGetOutputVec(int pos)
	{
		if(pos >= this.NET_OUT || pos < 0)
		{
			this.nnBarf("nnGetOutputVec", "Invalid position requested");
			return 0.0;
		}

		return this.outputVector[pos];
	}

	public double nnGetDesiredOut(int pos)
	{
		if(pos >= this.NET_OUT || pos < 0)
		{
			this.nnBarf("nnGetDesiredOut", "Invalid position requested");
			return 0.0;
		}

		return this.desiredOut[pos];
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
					return this.weightsL1[layerA][layerB];
			}

			case 2:
			{
				if(0<=layerA && layerA<NET_MID && 0<=layerB && layerB<NET_OUT)
					return this.weightsL2[layerA][layerB];
			}
		}

		this.nnBarf("nnGetWeightAt", "Address requested not initialised");
		return 0;
	}
}




