/**
 * 
 */
package uned.si2;

import robocode.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.math.BigDecimal;
import static robocode.util.Utils.normalRelativeAngleDegrees;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Robot001 - a robot by (your name here)
 */
public class Robot001 extends AdvancedRobot
{
	int distMoveOnHit = 50;	//Distancia a la que moverse cuando nos disparan

	/**
	 * run: Robot001's default behavior
	 */
	double moveAmount;
	int numHitWalls = 0;
	
	
	public void run() {
		// Initialization of the robot should be put here

		 setColors(Color.red,Color.blue,Color.green); // body,gun,radar
		 
		//Inicializamos la máxima cantidad de movimiento que tendrá el robot, 
		//dependiendo del tamaño del campo de batalla (visto en robot Walls)
		moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());	
		
		//Inicializamos el roboth para 

		// Robot main loop
//		turnLeft(getHeading());	//Giramos a izquierda hasta estar a 0 grados (Norte)	
//		ahead(moveAmount); //Mueve robot hacia delante moveAmount cantidad
//		turnLeft (getHeading() % 90); //Con esto movemos el robot a izquierda hasta 
		//la orientación de la pared más cercana, y no el Norte como en el anterior
	
		
		//Mejora: calcular cual es la pared más cercana según nuestro Heading y moverse
		//hacia ella, girando a derecha o izquierda según donde esté.
		System.out.println("Heading: " + getHeading());
		if (getHeading() % 90 >= 45){
			System.out.println("GetHeading%90: " + getHeading() % 90 + ", mayor 45");
			turnRight (90 - (getHeading () % 90));
		}
		else{
			System.out.println("GetHeading%90: " + getHeading() % 90 + ", menor 45");
			turnLeft (getHeading () % 90);
			}
					
		//ahead (moveAmount);
		//turnRight(90);

		System.out.println("Fin inicialización");
		while(true) {
			// Replace the next 4 lines with any behavior you would like

//			ahead(moveAmount);
//			turnRight(90);
			turnGunRight(5);
			
		}
	}

//Coordenadas del último robot escaneado
		int scannedX = Integer.MIN_VALUE;
		int scannedY = Integer.MIN_VALUE;
		
	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Replace the next line with any behavior you would like
		//Calculamos el ángulo al robot escaneado
		double angle = Math.toRadians((getHeading() + e.getBearing())%360);
		
		//Calculamos las coordenadas del robot escaneado
		scannedX = (int) (getX() + Math.sin(angle) * e.getDistance());
		scannedY = (int) (getY() + Math.cos(angle) * e.getDistance());

		setDebugProperty("lastScannedRobot", "[" + getTime() + "]" 
				+ e.getName() + " at " + roundTwoDec(e.getBearing()) + " degrees\n" 
				+ ", distance=" + roundTwoDec(e.getDistance())
				+ ", energy=" + roundTwoDec(e.getEnergy()));		

//		fire(1);
	//[sample.Fire] Si el otro robot está cerca y tenemos mucha vida
	//disparamos con todo
	if(e.getDistance() < 50 && getEnergy() > 50){
		fire(3);
	}	//Si no, disparamos fuerza 1
	else{
		fire(1);
	}
	//Llamamos de nuevo a Scan, antes de girar la torreta
	scan();
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		setDebugProperty("lastHitBy:", "[" + getTime() + "]:" + e.getName() 
				+ "with power of bullet " + e.getPower() 
				+ "\n velocity" + e.getVelocity() 
				+ " at " + e.getBearing() + " degrees");
		debugPaintHitBullet();
//		if (e.getBearing() == 0){
//			//El robot está enfrente de nosotros!!!
//			//Disparamos con todo
//			fire(1);
//			System.out.println("Fire 1!!!!");
//		}
//		if (e.getBearing() == 180)
//		{ //El robot está detras de nosotros!!!
//			// Giramos y disparamos con todo
//			turnRight(180);
//			fire(3);
//		}
		
		//[sample.Fire] Giramos perpendicularmente a la bala y 
		//nos movemos un poco
//		turnRight(normalRelativeAngleDegrees(90 - (getHeading() - e.getHeading())));
		turnRight(90 - (getHeading() - e.getHeading()));
	
		ahead(distMoveOnHit);
		distMoveOnHit *= -1;
		scan();		

	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		System.out.println("Hit Wall!!!!, giremos 180º");
		numHitWalls++;
		if (numHitWalls >= 2)
		{
			turnRight(180);
		}
//		back(20);

	}	
	
	//Pintar un cuadrado transparente encima del último robot escaneado
	public void onPaint(Graphics2D g){
		//Establecemos el color a pintar: color rojo medio transparente
		g.setColor(new Color(0xff, 0x00, 0x00, 0x80));
		
		//Establecemos el color del radio de acción del escaner
				
		//Dibujamos una línea desde nuestro robot hacia el robot escaneado
		g.drawLine(scannedX, scannedY, (int)getX(), (int)getY());
		
		//Dibujamos un círculo con centro en nuestro robot y radio el rango del escáner
		g.drawOval((int)getX() -600,(int) getY() -600, 1200, 1200);
		
		//Dibujamos un rectángulo relleno encima del robot escaneado, que lo cubre
		g.fillRect(scannedX - 20, scannedY - 20, 40, 40);
		
//		g.SetColor(new Color(0xff, 0xff, 0x80));
		//Dibujamos una línea desde nuestro robot, mostrando la posición del escaner
//		g.drawLine(, (int)getX(), (int)getY());


		//Dibujamos un arco que muestre donde está el frontal del robot
		g.setColor(new Color(0xff, 0x00, 0x00, 0x80));
		g.fillArc((int)getX()-50, (int)getY()-50, 100, 100, (int)getHeading()+30 , -60);
		

	}	

public double roundTwoDec(double d) {
	BigDecimal bigDec = new BigDecimal(d);
	bigDec.setScale(2, BigDecimal.ROUND_UP);
	return bigDec.doubleValue();
}

public void debugPaintHitBullet(){
	Graphics2D g = getGraphics();
	
	g.setColor(Color.orange);
	g.drawOval((int) (getX() - 55), (int)(getY() - 55), 110, 110);
//	g.drawOval((int) (getX() - 56), (int)(getY() - 56), 112, 112);
//	g.drawOval((int) (getX() - 59), (int)(getY() - 59), 118, 118);
//	g.drawOval((int) (getX() - 60), (int)(getY() - 60), 120, 120);			
}






}
