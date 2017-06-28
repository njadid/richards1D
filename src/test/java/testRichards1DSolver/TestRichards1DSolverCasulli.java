/*
 * GNU GPL v3 License
 *
 * Copyright 2016 Marialaura Bancheri
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package testRichards1DSolver;
import java.net.URISyntaxException;
import java.util.*;

import org.jgrasstools.gears.io.timedependent.OmsTimeSeriesIteratorReader;

import Richards1DSolver.*;

import org.junit.Test;
import richards_classes.ReadCsvTwoColumns;

/**
 * Test the {@link TestRichards1DSolver} module with the numerical experiment shown in Casulli's and Zanolli's paper 2010.
 * 
 * @author Niccolo' Tubini, Francesco Serafin
 */
public class TestRichards1DSolverCasulli {

	@Test
	public void Test() throws Exception {


		String startDate = "1991-09-18 00:00" ;
		String endDate = "1992-07-20 00:00";
		int timeStepMinutes = 60*24;
		String fId = "ID";


		String pathTopBC ="resources/Input/Casulli_TopBoundaryCondition.csv";
		String pathBottomBC ="resources/Input/Casulli_BottomBoundaryCondition.csv";
		String pathIC = "resources/Input/InitialConditionHydrostatic.csv";

		OmsTimeSeriesIteratorReader topBCReader = getTimeseriesReader(pathTopBC, fId, startDate, endDate, timeStepMinutes);
		OmsTimeSeriesIteratorReader bottomBCReader = getTimeseriesReader(pathBottomBC, fId, startDate, endDate, timeStepMinutes);

		ReadCsvTwoColumns readIC = new ReadCsvTwoColumns();
		readIC.setFilePath(pathIC);
		readIC.process();
		double[] iC = readIC.getSuction();
		double[] depth = readIC.getDepth();

		Richards1DSolver R1DSolver = new Richards1DSolver();

		R1DSolver.ks = 0.062/86400;
		R1DSolver.thetaS = 0.410;
		R1DSolver.thetaR = 0.095;
		R1DSolver.n = 1.31;
		R1DSolver.alpha = 1.9;
		/**
		 * In this experiment Van Genuchten's model is used
		 */
		R1DSolver.lambda = -999;
		R1DSolver.psiE = -999;
		R1DSolver.rMedian = -999;
		R1DSolver.sigma = -999;
		R1DSolver.soilHydraulicModel = "VanGenuchten";
		R1DSolver.topBCType = "Top Dirichlet";
		R1DSolver.bottomBCType = "Bottom Dirichlet";
		R1DSolver.delta = 0;
		R1DSolver.spaceBottom = 2.0;
		/**
		 * The time step is 1000 seconds and it is not consistent with the time series of the boundary condition
		 * The time series is necessary for the jgrasstool
		 */
		R1DSolver.tTimestep = 1000; 
		R1DSolver.newtonTolerance = Math.pow(10,-10);
		R1DSolver.iC = iC;
		R1DSolver.depth = depth;
		R1DSolver.dir = "resources/Output";
		R1DSolver.nestedNewton = 1;
		while( topBCReader.doProcess  ) {

			topBCReader.nextRecord();	
			HashMap<Integer, double[]> bCValueMap = topBCReader.outData;
			R1DSolver.inTopBC= bCValueMap;


			bottomBCReader.nextRecord();
			bCValueMap = bottomBCReader.outData;
			R1DSolver.inBottomBC = bCValueMap;

			R1DSolver.inCurrentDate = topBCReader.tCurrent;
			
			R1DSolver.solve();		

		}

		topBCReader.close();
		bottomBCReader.close();
	}

	private OmsTimeSeriesIteratorReader getTimeseriesReader( String inPath, String id, String startDate, String endDate,
			int timeStepMinutes ) throws URISyntaxException {
		OmsTimeSeriesIteratorReader reader = new OmsTimeSeriesIteratorReader();
		reader.file = inPath;
		reader.idfield = "ID";
		reader.tStart = startDate;
		reader.tTimestep = timeStepMinutes;
		reader.tEnd = endDate;
		reader.fileNovalue = "-9999";
		reader.initProcess();
		return reader;
	}
	/* Per la lettura della condizione iniziale con la vecchia formattazione
	private double[] ReadAndStoreDouble(String filePath) {

		double[] readVector;
		List<Double> list = new ArrayList<>();	

		try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
			list = stream.
					filter(line -> !line.startsWith("!")).
					flatMap(line->Arrays.stream(line.split(" "))).
					map(Double::valueOf).
					collect(Collectors.toList());
		} catch (IOException e) { 
			e.printStackTrace();
		}

		System.out.println("Reading completed");


		readVector = new double[list.size()];
		for(int i = 0;i < readVector.length;i++){
			readVector[i] = list.get(i);
		}

		return readVector;

	}
	*/
}