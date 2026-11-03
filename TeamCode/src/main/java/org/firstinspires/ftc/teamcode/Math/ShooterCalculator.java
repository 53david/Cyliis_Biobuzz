package org.firstinspires.ftc.teamcode.Math;

import static org.firstinspires.ftc.teamcode.Components.Shooter.Flywheel.idlePower;
import static org.firstinspires.ftc.teamcode.Components.Shooter.Hood.pos;
import static org.firstinspires.ftc.teamcode.Components.Shooter.Turret.tx;
import static org.firstinspires.ftc.teamcode.Components.Shooter.Turret.ty;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Components.Shooter.Turret;
import org.firstinspires.ftc.teamcode.Wrappers.Odo;
@Config
public class ShooterCalculator {
    public static double[]v = {
                1340,
                1340,
                1340,
                1360,
                1360,
                1380,
                1380,
                1380,
                1400,
                1420,
                1440,
                1460,
                1480,
                1480,
                1480,
                1480,
                1480,
                1480,
    };
    public static double[]h = {
            0.3425,
            0.36,
            0.38,
            0.41,
            0.41,
            0.43,
            0.435,
            0.44,
            0.46,
            0.47,
            0.47,
            0.48,
            0.48,
            0.48,
            0.48,
            0.48,
            0.48,

    };
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
        distance = Math.sqrt((targetX - x)*(targetX - x) + (targetY - y)*(targetY - y))-50;
        if (x > 1680) distance+=50;
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
        if (distance<=800) distance = 800;
        int i= Math.min((int) (distance/100-8),v.length-1);
        return v[i] + (v[Math.min(i+1,v.length-1)]-v[i]) * (distance % 100/100);
    }
    public static double getHoodAngle(){
        if (distance<=800) distance = 800;
        int i= Math.min((int) (distance/100-8),h.length-1);
        return h[i] + (h[Math.min(i+1,h.length-1)]-h[i]) * (distance % 100/100);
    }

}
