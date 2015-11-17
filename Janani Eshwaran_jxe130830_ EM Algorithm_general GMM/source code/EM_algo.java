import java.util.ArrayList;
import java.util.Random;

// Class EM_Algo
public class EM_algo {
	static ArrayList<Double> data = new ArrayList<Double>();
	static int variance_type;
	static int k;
	static double prior[];
	static double mean[];
	static double variance[];
	static int iteration = 0;
	static double likelihood[][];

	EM_algo(ArrayList<Double> data,int k)

	{
		this.data = data;
		this.k=k;
		this.variance_type=0;
		prior= new double[k];
		mean = new double[k];
		variance = new double[k];
		likelihood = new double [data.size()][k];
		
	}
	//Method to perform EM Algorithm
	public static void start_EM()
	{
		System.out.println("Starting EM algo");
		calculate_Prior();
		System.out.println("Step: 1: Finished calculating prior");
		initial_Mean_Variance();
		System.out.println("Initial Mean, variance and prior values");
		print_mean_variance_prior();
		System.out.println("Step :2 :Finished calculating initial mean and variance");
		System.out.println("Step :3 :Performing EM algo");
		EM_Convergence();
		System.out.println("Step :4 :EM algo converged");
		System.out.println("Final Mean, variance and prior values");
		print_mean_variance_prior();

	}// end of method start_EM




	public static void print_mean_variance_prior() {
		System.out.println();
		System.out.println(" Mean values");
		for (int i = 0; i < k; i++) {
			System.out.println(mean[i]);
		}
		System.out.println();
		System.out.println(" Variance values");
		for (int i = 0; i < k; i++) {
			System.out.println(variance[i]);
		}
		System.out.println();
		System.out.println(" Prior values per Cluster:");
		for (int i = 0; i < k; i++) {
			System.out.println(prior[i]);
		}
	}





	//Method to perform EM Algorithm until convergence of mean 
	public static void EM_Convergence()
	{
		boolean flag =false;
		double log_val_prev=0.0, log_val_curr=0.0;
		//System.out.println("Data size: " + data.size());
		do
		{

			E_Step_calculation();
			//	System.out.println("Finsished E Step");
			log_val_prev = calculate_log_likelihood ();

			//	System.out.println("log_val_prev ="+ log_val_prev);

			M_Step_calculation();
			//	System.out.println("Finsished M Step");
			log_val_curr = calculate_log_likelihood();
			//		System.out.println("log_val_curr ="+ log_val_curr);

			iteration++;

			
			//double diff = log_val_curr -log_val_prev;

			//if (log_val_prev == log_val_curr )
			//			if(diff < 0.001)
			//			{
			//				convergence= true;
			//			}
			//
			//			convergence = false;
			//			
			//			System.out.println("convergence :" +convergence);
			//		
			
			if ((log_val_curr  == log_val_prev)) {
				System.out.println("convergenced" );
				System.out.println("No of Iterations taken :" +iteration);

				flag= true;
			}
			else
				flag= false;
			
		}while(!flag);



	}// end of method EM_Convergence


	// Methos to perform M_Step
	public static void M_Step_calculation()
	{
		double cluster_likelihood[]= new double[k];
		// calculate new prior and mean
		for (int j = 0; j < k; j++) {
			double sum=0.0;
			for (int i = 0; i < data.size(); i++) {
				cluster_likelihood[j] += likelihood[i][j];
				sum += data.get(i)*likelihood[i][j];

			}// end of for i
			prior[j]= cluster_likelihood[j] /data.size(); // updating new prior for each cluster j

			if(sum!=0 && cluster_likelihood [j]!=0)
				mean[j]= sum/cluster_likelihood[j]; //updating new mean for each cluster j
			else
				mean[j]=0.0;
		}// end of for j

		// calculate new variance


		for (int j = 0; j < k; j++) {
			double sum = 0.0;
			for (int i = 0; i < data.size(); i++) {

				double temp =(data.get(i) - mean[j]);
				temp = Math.pow(temp, 2);
				sum += likelihood[i][j] *temp;
			}
			variance[j] = (sum / cluster_likelihood[j]); //updating new variance for each cluster j
		}


	} // end of method M_Step





	// method to calculate log likelihood

	public static double calculate_log_likelihood()
	{
		double loglikelihood=0.0;
		for(int i=0;i<data.size();i++){
			double temp=0;
			for(int j=0;j<k;j++){
				temp +=prior[j]*( cal_gaussian(data.get(i), mean[j],	variance[j])*prior[j]);
			}
			loglikelihood+=Math.log(temp);
		}
		return loglikelihood;
	}// end of method calculate_log_likelihood



	// Method to perform Estep
	public static void E_Step_calculation()
	{
		// for each data point
		for(int i=0;i<data.size();i++)
		{
			double denom_val=0.0;
			// for each cluster
			for(int j=0;j<k;j++)
			{
				//	System.out.println("prior= " + prior[j]);
				double gau= cal_gaussian(data.get(i), mean[j],variance[j]);
				double data_likelihood = prior[j]* gau;
				//	System.out.println("cal_gaussian= " + gau);
				//	System.out.println("data_likelihood= " + data_likelihood);
				denom_val += data_likelihood;
				likelihood[i][j]=data_likelihood;


			}// end for j
			for (int j = 0; j < k; j++) {

				//				if(denom_val != 0)
				//				{
				//					//System.out.println("Denom_val= " + denom_val);
				likelihood[i][j] = likelihood[i][j] / denom_val;
				//	System.out.println("likelihood= i= "+i+" j= "+j+ ": " + likelihood[i][j]);
				//				}
				//				else
				//					{
				//					likelihood[i][j]=0.0;
				//					}
			}// end of for l


		}// end of for i

	}




	// method to calculate gaussian
	private static double cal_gaussian(double xi, double mean, double var) {
			double gaussian_val = 0.0;
		
			double temp1 = -((xi - mean) * (xi - mean)) / (2 * var);
			double temp2 = Math.exp(temp1);
			double temp3 = (Math.sqrt(2 * Math.PI) * Math.sqrt(Math.abs(var)));
			if(temp3!=0.0)
			{
				gaussian_val= temp2/temp3;
			}
			else
			{
				gaussian_val =0.0;
			}
				return gaussian_val;

	}// end of cal_gaussian

	// Method to calculate prior
	public static void calculate_Prior()
	{
		for(int i=0;i<k;i++)
		{
			prior[i]=1.0/k;
		}// end of for i
	}// end of function calculate_Prior


	// Method to set initial Mean and variance
	public static void initial_Mean_Variance()
	{
		// set random datapoints in the data ararylist as mean for k clusters by selecting k random points in the dataset
		Random r = new Random();
		int total_data = data.size();
		for(int i=0;i<k;i++)
		{
			mean[i]=1.5+Math.random()*28.0;
			//	mean[i]=data.get(new Random().nextInt(data.size()));
		}// end of for i

		// now calculate the variance for k clusters 

		double sum_temp = 0.0;
		// first calculate mean
		for(int j=0;j<data.size();j++)
		{
			sum_temp += data.get(j);
		}
		double mean_temp =sum_temp/(double)data.size();
		// second use variance formula 
		double variance_temp =0.0, variance_sum =0.0, term =0.0 ;
		for( int k=0;k<data.size();k++)
		{
			variance_sum +=(data.get(k)-mean_temp)* (data.get(k)-mean_temp) ;


		}

		variance_temp= variance_sum/data.size();

		// third - intialize each cluster with variance value

		if(variance_type ==1)// then initialize variance as 1.0
		{
			for(int l=0;l<k;l++)
			{
				variance[l]=1.0;

			}
		}// end of if
		else
		{
			for(int l=0;l<k;l++)
			{
				variance[l]= variance_temp* (1.0+ 0.5);
			}
		}// end of else

	} // end of method initial_Mean_Variance



}// end of class EM_Algo
