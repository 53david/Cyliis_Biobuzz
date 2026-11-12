package org.firstinspires.ftc.teamcode.Math;


import static org.firstinspires.ftc.teamcode.Components.Shooter.Turret.tx;
import static org.firstinspires.ftc.teamcode.Components.Shooter.Turret.ty;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Components.Shooter.Flywheel;
import org.firstinspires.ftc.teamcode.Components.Shooter.Hood;
import org.firstinspires.ftc.teamcode.Components.Shooter.Turret;
import org.firstinspires.ftc.teamcode.Wrappers.Odo;
@Config
public class ShooterCalculator {
    boolean ok = true;
    public double height = 150;
    public static double[]v = {
            1140,
            1140,
            1140,
            1120,
            1140,
            1180,
            1180,
            1170,
            1190,
            1190,
            1230,
            1260,
            1250,
            1240,
            1240,
            1260,
            1260,
            1260,
    };

    public static double[]h = {
            0,
            0,
            0,
            0,
            0,
            0,
            0.015,
            0.04,
            0.058,
            0.082,
            0.12,
            0.13,
            0.13,
            0.13,
            0.13,
            0.13,
            0.13,

    };
    public double targetX = 0,targetY = 0,velocity;
    double hoodAngle = 0;
    public static double ax = 0, ay = 0, t = 1,distance = 1,x,y;
    public ShooterCalculator(){
        ok = true;
    }
    public void update(){
        hoodAngle = Math.PI/2 - ((getHoodAngle())*(355 * Math.PI/180)) * 1/3 - 5 * Math.PI/180;
        t = Range.clip(distance/((Flywheel.getAngularVelocity() * 72) * Math.cos(hoodAngle)),0.0005,5);
        ax = (Odo.xRobotVelocity-Odo.prevVelX)/t;
        ay = (Odo.yRobotVelocity-Odo.prevVelY)/t;
        targetX = Turret.getTargetX();
        targetY = Turret.getTargetY();
        x = Odo.x; //;+ (Odo.getVelX() * t) + (ax * (t * t)/2) + (tx * Math.cos(Odo.getHeading()) - ty * Math.sin(Odo.getHeading()));
        y = Odo.y; // (Odo.getVelY() * t) + (ay * (t * t)/2) + (tx * Math.sin(Odo.getHeading()) + ty * Math.cos(Odo.getHeading()));
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
        if (distance<=300) distance = 300;
        int i= Math.min((int) (distance/100-3),v.length-1);
        return v[i] + (v[Math.min(i+1,v.length-1)]-v[i]) * (distance % 100/100);
    }
    public static double getHoodAngle(){
        if (distance<=300) distance = 300;
        int i= Math.min((int) (distance/100-3),h.length-1);
        return h[i] + (h[Math.min(i+1,h.length-1)]-h[i]) * (distance % 100/100);
    }

}
