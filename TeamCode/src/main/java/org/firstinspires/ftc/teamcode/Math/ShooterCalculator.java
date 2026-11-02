package org.firstinspires.ftc.teamcode.Math;

import static org.firstinspires.ftc.teamcode.Components.Shooter.Flywheel.idlePower;
import static org.firstinspires.ftc.teamcode.Components.Shooter.Hood.pos;
import static org.firstinspires.ftc.teamcode.Components.Shooter.Turret.tx;
import static org.firstinspires.ftc.teamcode.Components.Shooter.Turret.ty;

import com.qualcomm.robotcore.util.Range;

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
        double[]v = {
                1280,
                1350,
                1360,
                1360,
                1380,
                1400,
                1420,
                1420,
                1440,
                1460,
                1470,
                1480,
        };
        if (distance<=800) distance = 800;
        int i= Math.min((int) (distance/100-8),v.length-1);
        return v[i] + (v[Math.min(i+1,v.length-1)]-v[i]) * (distance % 100/100);
    }
    public static double getHoodAngle(){
        double[]v = {
                0.33,
                0.395,
                0.41,
                0.415,
                0.4295,
                0.431,
                0.435,
                0.438,
                0.45,
                0.452,
                0.456,
                0.46,
        };
        if (distance<=800) distance = 800;
        int i= Math.min((int) (distance/100-8),v.length-1);
        return v[i] + (v[Math.min(i+1,v.length-1)]-v[i]) * (distance % 100/100);
    }

}
