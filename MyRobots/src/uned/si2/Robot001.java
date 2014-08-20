/**
 * 
 */
package uned.si2;

import robocode.*;
import java.awt.Color;
import java.awt.Graphics2D;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Robot001 - a robot by (your name here)
 */
public class Robot001 extends AdvancedRobot
{
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
					
		ahead (moveAmount);
		turnRight(90);

		System.out.println("Fin inicialización");
		while(true) {
			// Replace the next 4 lines with any behavior you would like
/*			System.out.println("Inicio adelante 100 y giro cañón 360");
			ahead(100);
			turnGunRight(360);
			System.out.println("Inicio atrás 100 y giro cañón 360");
			back(100);
			turnGunRight(360);
*/			
			ahead(moveAmount);
//			turnRight(90);
			
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


/*		System.out.println("*******************************************");
		System.out.println("Tiempo: " + getTime());
		System.out.println("Robot detectado: " + e.getName() + " - Posición: ángulo=" + e.getBearing() + 
					" ,distancia: " + e.getDistance()); 
		System.out.println("Movimiento: dirección=" + e.getHeading() + " ,velocidad=" + e.getVelocity());
		System.out.println("Energía: " + e.getEnergy());
		System.out.println("*******************************************");
		fire(1);
*/		


		
		
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		//back(10);
		System.out.print("Nos atacan!!!" );
		System.out.println ("Fue el robot: " + e.getName());
		System.out.println("Bullet: f=" + e.getPower() + ",v="+ e.getVelocity());
		System.out.println("Bullet Bearing=" + e.getBearing() + 
			",BearingRads=" + e.getBearingRadians());
		if (e.getBearing() == 0){
			//El robot está enfrente de nosotros!!!
			//Disparamos con todo
			fire(3);
			System.out.println("Fire 3!!!!");
		}
		if (e.getBearing() == 180)
		{ //El robot está detras de nosotros!!!
			// Giramos y disparamos con todo
			turnRight(180);
			fire(3);
			}
			
//		turnLeft(180);
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
	}	

}
