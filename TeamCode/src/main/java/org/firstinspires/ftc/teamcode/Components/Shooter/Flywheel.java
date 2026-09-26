package org.firstinspires.ftc.teamcode.Components.Shooter;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.teamcode.Wrappers.Hardware;
import org.firstinspires.ftc.teamcode.Wrappers.Odo;

@Config
public class Flywheel {
    CRServo shoot1,shoot2;
    DcMotorEx encoder;
    public static double Kp = 0;
    public static double Ks = 0;
    public static double Kv = 0;
    public static double Ka = 0;
    public static double shootPower = 0,idlePower = 1450;
    public static double currentVelocity = 0,targetVelocity =0,prevVelocity = 0;
    public enum State{
        IDLE(idlePower),
        SHOOT(shootPower);
        double power;
        State(double power){
            this.power = power;
        }

    }
    public static double errorThreshold = 60;
    public State state = State.SHOOT;
    public static double rpm = 0;
    public Flywheel(){
        encoder = Hardware.mch3;
        shoot1 = Hardware.sch2;
        shoot2 = Hardware.sch3;
        shoot1.setDirection(DcMotorSimple.Direction.REVERSE);
        shoot2.setDirection(DcMotorSimple.Direction.FORWARD);
    }
    public void update(){
        targetVelocity = state.power;
        currentVelocity = encoder.getVelocity();
        updateState();
        updatePower();
        updateHardware();
        prevVelocity = currentVelocity;

    }
    private void updateState(){
        switch (state){
            case IDLE:
                break;
            case SHOOT:
                shootPower = calculatePower(Odo.distance);
                break;
        }
    }
    private void updatePower(){
        State.SHOOT.power = shootPower;
        State.IDLE.power = idlePower;
    }
    private void updateHardware(){
        rpm = Kv * state.power + Ks * Math.signum(state.power- currentVelocity)
                + (state.power-currentVelocity) * Kp;
        if (state.power > currentVelocity) rpm += Ka*(prevVelocity-currentVelocity);
        else rpm += Ka * (currentVelocity-prevVelocity);

        shoot1.setPower(rpm);
        shoot2.setPower(rpm);

    }
    public boolean isReady(){
        return Math.abs(currentVelocity-state.power) < errorThreshold;
    }
    public double getVelocity(){
        return currentVelocity;
    }
    public double getTargetVelocity(){
        return state.power;
    }
    public void setState(State state){
        this.state = state;
    }
    public State getState(){
        return state;
    }
    public double calculatePower(double distance){
       return 0;
    }

}