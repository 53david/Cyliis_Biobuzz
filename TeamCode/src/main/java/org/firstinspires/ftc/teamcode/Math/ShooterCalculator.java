package org.firstinspires.ftc.teamcode.Math;

import static org.firstinspires.ftc.teamcode.Components.Shooter.Turret.tx;
import static org.firstinspires.ftc.teamcode.Components.Shooter.Turret.ty;

import org.firstinspires.ftc.teamcode.Components.Shooter.Turret;
import org.firstinspires.ftc.teamcode.Wrappers.Odo;

public class ShooterCalculator {
    public double targetX = 0,targetY = 0,velocity;
    public static double ax = 0, ay = 0, t = 0,distance,x,y;
    public ShooterCalculator(){}
    public void update(){

        ax = (Odo.xRobotVelocity-Odo.prevVelX)/t;
        ay = (Odo.yRobotVelocity-Odo.prevVelY)/t;
        targetX = Turret.getTargetX();
        targetY = Turret.getTargetY();
        x = Odo.x;//(Odo.x + (Odo.getVelX() * t) + (ax * (t * t)/2) + (tx * Math.cos(Odo.getHeading()) - ty * Math.sin(Odo.getHeading())));
        y = Odo.y;//(Odo.y + (Odo.getVelY() * t) + (ay * (t * t)/2) + (tx * Math.sin(Odo.getHeading()) + ty * Math.cos(Odo.getHeading())));
        distance = Math.sqrt((targetX - x)*(targetX - x) + (targetY - y)*(targetY - y));
        velocity = getVelocity();
        updateTime();

    }
    public void updateTime(){
        double[] v = {
                0.35,
                0.365,
                0.37,
                0.085,
                0.1,
        };
        int i =  Math.abs((int)velocity/100);
        if (i > v.length) i = v.length - 1;
        t = v[i] + (v[Math.min(i+1,v.length-1)] - v[i]) * (velocity % 100)/100;
    }
    public static double getTime(){
        return t;
    }
    public static double getDistance(){
        return distance;
    }
    public static double getAccX(){
        return ax;
    }
    public static double getAccY(){
        return ay;
    }
    public static double getVelocity(){
        return 0.221519*distance+1115.82278;
    }
    public static double getHoodAngle(){
       return 0.000205696*distance+0.131835;
    }

}
