package uned.si2;

import robocode.Robot;
import robocode.ScannedRobotEvent;

public class TestRobotAgilrod1 extends Robot {

	public void run(){
		turnLeft(getHeading() % 90);
		turnGunRight(90);
		
		while(true){
			ahead(1000);
			turnRight(90);
		}
	}
	
	public void OnScannedRobot(ScannedRobotEvent e){
		fire(1);
	}
}
