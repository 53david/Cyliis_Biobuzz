package org.firstinspires.ftc.teamcode.Math;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Components.Shooter.Flywheel;
import org.firstinspires.ftc.teamcode.Components.Shooter.Turret;
import org.firstinspires.ftc.teamcode.Wrappers.Odo;
@Config
public class ShooterCalculator {
    ElapsedTime timer;
    public static double velocity = 1260, hoodPos = 0.11;
    public static double[]v = {
            1140,
            1140,
            1120,
            1120,
            1120,
            1140,
            1140,
            1140,
            1140,
            1140,
            1160,
            1160,
            1160,
            1160,
            1180,
            1180,
            1200,
            1200,
    };

    public static double[]h = {
            0,
            0,
            0,
            0,
            0.02,
            0.03,
            0.03,
            0.04,
            0.05,
            0.05,
            0.05,
            0.07,
            0.0875,
            0.09,
            0.092,
            0.13,
            0.14,
            0.15,
            0.16,

    };
    public double targetX = 0,targetY = 0;
    double hoodAngle = 0;
    public static double ax = 0, ay = 0, t = 0,distance = 1,x,y;
    public ShooterCalculator(){
        timer = new ElapsedTime();
        timer.startTime();
        timer.reset();
    }
    public void update(){

        double lt = timer.seconds();
        timer.reset();

        hoodAngle = Math.PI/2 - (getHoodAngle() * Math.PI*2) * 3 - 5 * Math.PI/180;

        ax = (Odo.xRobotVelocity-Odo.prevVelX)/lt;
        ay = (Odo.yRobotVelocity-Odo.prevVelY)/lt;

        targetX = Turret.getTargetX();
        targetY = Turret.getTargetY();

        x = Odo.x + (Odo.xRobotVelocity * t) + (ax * (t * t)/2);
        y = Odo.y + (Odo.yRobotVelocity * t) + (ay * (t * t)/2);

        distance = Math.sqrt((targetX - x)*(targetX - x) + (targetY - y)*(targetY - y));
        t = distance/((Flywheel.getAngularVelocity() * 72) * Math.cos(hoodAngle));
        t =0;


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
        return velocity;
        //return v[i] + (v[Math.min(i+1,v.length-1)]-v[i]) * (distance % 100/100);
    }
    public static double getHoodAngle(){
        if (distance<=300) distance = 300;
        int i= Math.min((int) (distance/100-3),h.length-1);
        return hoodPos;
        //return h[i] + (h[Math.min(i+1,h.length-1)]-h[i]) * (distance % 100/100);
    }

}
