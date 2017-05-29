package richards_classes;

public class TopBoundaryConditionDirichlet extends BoundaryCondition{

	public double upperDiagonal(double bC, double kP, double kM, double spaceDeltaP, double spaceDeltaM, double tTimestep) {
		this.bC = bC;
		this.kP = kP;
		this.kM = kM;
		this.spaceDeltaP = spaceDeltaP;
		this.spaceDeltaM = spaceDeltaM;
		this.tTimestep = tTimestep;
		this.term = 0;

		return term;
	}
	
	public double mainDiagonal(double bC, double kP, double kM, double spaceDeltaP, double spaceDeltaM, double tTimestep) {
		this.bC = bC;
		this.kP = kP;
		this.kM = kM;
		this.spaceDeltaP = spaceDeltaP;
		this.spaceDeltaM = spaceDeltaM;
		this.tTimestep = tTimestep;

		this.term = this.tTimestep/(this.spaceDeltaM/2+this.spaceDeltaP)*1/this.spaceDeltaM*this.kM + this.tTimestep/(this.spaceDeltaM/2+this.spaceDeltaP)*1/this.spaceDeltaP*this.kP; 
		return term;

	}
	
	public double lowerDiagonal(double bC, double kP, double kM, double spaceDeltaP, double spaceDeltaM, double tTimestep) {
		this.bC = bC;
		this.kP = kP;
		this.kM = kM;
		this.spaceDeltaP = spaceDeltaP;
		this.spaceDeltaM = spaceDeltaM;
		this.tTimestep = tTimestep;
		
		this.term = -this.tTimestep/(this.spaceDeltaM/2+this.spaceDeltaP)*1/this.spaceDeltaM*this.kM;

		return term;

	}

	public double rightHandSide(double bC, double kP, double kM, double spaceDeltaP, double spaceDeltaM, double tTimestep) {
		this.bC = bC;
		this.kP = kP;
		this.kM = kM;
		this.spaceDeltaP = spaceDeltaP;
		this.spaceDeltaM = spaceDeltaM;
		this.tTimestep = tTimestep;
		
		this.term = this.tTimestep/(this.spaceDeltaM/2+this.spaceDeltaP) * this.kP - this.tTimestep/(this.spaceDeltaM/2+this.spaceDeltaP)*this.kM + this.kP * this.tTimestep/(this.spaceDeltaM/2+this.spaceDeltaP)*1/this.spaceDeltaP * this.bC;

		return term;

	}
}
