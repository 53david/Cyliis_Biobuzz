package org.firstinspires.ftc.teamcode.Components.Chassis;

import static org.firstinspires.ftc.teamcode.Wrappers.Hardware.pp;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import org.firstinspires.ftc.teamcode.Math.PIDController;
import org.firstinspires.ftc.teamcode.Wrappers.Hardware;
import org.firstinspires.ftc.teamcode.Wrappers.Odo;
import org.firstinspires.ftc.teamcode.Robot.Pose2D;
@Config
public class Chassis{

    public enum State{
        DRIVE , PID;
    }
    public State state;
    public boolean usePredictiveBreaking = true;
    CRServo frontLeft,frontRight,backLeft,backRight;
    public static boolean stop = false;
    public  double targetX , targetY ,x=0 ,y=0,multiplier = 1;
    public static double targetHeading;
    public static double error;
    double rotation,heading;
    public boolean usingTargetHeading=true;

    public static double lateralMultiplier=1.5;
    public static  double realHeading;

    public static double kp=0.0065 , ki=0, kd=0;
    public static double KP=1 , KI=0, KD=0.1;
    public PIDController controllerX=new PIDController(kp, 0, kd);
    public PIDController controllerY=new PIDController(kp, 0, kd);
    public PIDController controllerHeading=new PIDController(KP, 0, KD);

    public Chassis(State initialState)
    {
        frontLeft= Hardware.sch2;
        frontRight= Hardware.sch0;
        backLeft=Hardware.sch3;
        backRight=Hardware.sch1;

        state=initialState;
        backLeft.setDirection(DcMotorEx.Direction.REVERSE);
        frontLeft.setDirection(DcMotorEx.Direction.REVERSE);
    }
    public void setMode(State state)
    {
        this.state =state;
    }
    public void turnOff(){
        stop = true;
    }
    public void turnOn(){
        stop = false;
    }

    public boolean inPosition(double ErrorX , double ErrorY , double ErrorRx)
    {

        double heading= Odo.getHeading();
        if(heading<0)realHeading=Math.abs(heading);
        else realHeading=2*Math.PI-heading;

        error=targetHeading-realHeading;
        if(Math.abs(error)>Math.PI)
            error=-Math.signum(error)*(2*Math.PI-Math.abs(error));

        if(Math.abs(targetX-pp.getPosX(DistanceUnit.MM))<ErrorX
                && Math.abs(targetY-pp.getPosY(DistanceUnit.MM))<ErrorY
                && Math.abs(error)<ErrorRx)
            return true;
        return false;
    }

    public void setTargetVector(double x , double y , double rx)
    {
        if (stop) {
            frontLeft.setPower(0);
            backLeft.setPower(0);
            frontRight.setPower(0);
            backRight.setPower(0);
            return;
        }
        x*=lateralMultiplier;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx) , 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;
        frontLeft.setPower(frontLeftPower * multiplier);
        backLeft.setPower(backLeftPower * multiplier);
        frontRight.setPower(frontRightPower * multiplier);
        backRight.setPower(backRightPower * multiplier);

    }
    public void setTargetPosition(double x , double y , double heading)
    {
        targetX=x;
        targetY=y;
        targetHeading=heading-Math.floor((heading/ (Math.PI*2)))*Math.PI*2;
        usingTargetHeading=true;
        usePredictiveBreaking = true;

    }

    public void setTargetSpecialPosition(double targetX , double targetY , double targetHeading)
    {
        this.targetX=targetX;
        this.targetY=targetY;
        rotation=targetHeading;
        usingTargetHeading=false;
        usePredictiveBreaking = true;
    }
    public void setTargetSpecialPosition(Pose2D position)
    {
        this.targetX=position.x;
        this.targetY=position.y;
        rotation=targetHeading;
        usingTargetHeading=false;
        usePredictiveBreaking = true;
    }
    public void setTargetPosition(Pose2D position)
    {
        targetX=position.x;
        targetY=position.y;
        targetHeading=position.heading;
        usingTargetHeading=true;
        usePredictiveBreaking = true;
    }
    public void setTargetPosition(double x , double y , double heading, boolean usePredictiveBreaking)
    {
        targetX=x;
        targetY=y;
        targetHeading=heading-Math.floor((heading/ (Math.PI*2)))*Math.PI*2;
        usingTargetHeading=true;
        this.usePredictiveBreaking = usePredictiveBreaking;


    }

    public void setTargetSpecialPosition(double targetX , double targetY , double targetHeading,boolean usePredictiveBreaking)
    {
        this.targetX=targetX;
        this.targetY=targetY;
        rotation=targetHeading;
        usingTargetHeading=false;
        this.usePredictiveBreaking = usePredictiveBreaking;

    }
    public void setTargetSpecialPosition(Pose2D position, boolean usePredictiveBreaking)
    {
        this.targetX=position.x;
        this.targetY=position.y;
        rotation=targetHeading;
        usingTargetHeading=false;
        this.usePredictiveBreaking = usePredictiveBreaking;

    }
    public void setTargetPosition(Pose2D position, boolean usePredictiveBreaking)
    {
        targetX=position.x;
        targetY=position.y;
        targetHeading=position.heading;
        usingTargetHeading=true;
        this.usePredictiveBreaking = usePredictiveBreaking;
    }
    public void setHeadingPIDCoefficients(double Kp, double Ki, double Kd){
        KP = Kp;
        KI = Ki;
        KD = Kd;
    }
    public void setPIDCoefficients(double Kp, double Ki, double Kd){
        kp = Kp;
        ki = Ki;
        kd = Kd;
    }
    public void setHeadingPIDCoefficients(PIDController pidController){
        KP = pidController.kp;
        KI = pidController.ki;
        KD = pidController.kd;
    }
    public void setPIDCoefficients(PIDController pidController){
        kp = pidController.kp;
        ki = pidController.ki;
        kd = pidController.kd;
    }
    public void updatePID(){
        controllerX.kp=kp;
        controllerY.kp=kp;

        controllerX.kd=kd;
        controllerY.kd=kd;

        controllerHeading.kp=KP;
        controllerHeading.kd=KD;
    }

    public void update() {
        updatePID();
        if (state!=State.PID){
            return;
        }
        if (Double.isNaN(Odo.x) || Double.isNaN(Odo.y) || Double.isNaN(Odo.heading)) {
            return;
        }
        double posX, posY;
        if (usePredictiveBreaking){
            posX = Odo.predictedX;
            posY = Odo.predictedY;
        }
        else {
            posX = Odo.x;
            posY = Odo.y;
        }

        x = controllerX.calculate(targetX, posX);
        y = -controllerY.calculate(targetY, posY);

        heading = Odo.getHeading();
        if (heading < 0) realHeading = Math.abs(heading);
        else realHeading = 2 * Math.PI - heading;

        error = targetHeading - realHeading;
        if (Math.abs(error) > Math.PI) {
            error = -Math.signum(error) * (2 * Math.PI - Math.abs(error));
        }
        rotation = controllerHeading.calculate(error, 0.0);

        setTargetVector(y * Math.cos(-heading) - x * Math.sin(-heading), y * Math.sin(-heading) + x * Math.cos(-heading), rotation);
    }

}