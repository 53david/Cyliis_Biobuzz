package org.firstinspires.ftc.teamcode.Components.Shooter;


import static org.firstinspires.ftc.teamcode.Wrappers.Hardware.Voltage;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Math.ShooterCalculator;
import org.firstinspires.ftc.teamcode.Wrappers.Hardware;

@Config
public class Flywheel {
    public static double offset = 0;
    public DcMotorEx encoder,shoot1,shoot2;
    public static double Kp = 0.0005;
    public static double Ks = 0;
    public static double Kv = 0.00041;
    public static double shootPower = 0,idlePower = 1400;
    public static double currentVelocity = 0,targetVelocity =0,prevVelocity = 0;
    public static double x = 0;
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
        shoot1 = Hardware.mch1;
        shoot2 = Hardware.mch0;

        shoot1.setDirection(DcMotorEx.Direction.REVERSE);
        shoot2.setDirection(DcMotorEx.Direction.FORWARD);
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
                shootPower = ShooterCalculator.getVelocity() + offset;
                break;
        }
    }
    private void updatePower(){
        State.SHOOT.power = shootPower;
        State.IDLE.power = idlePower;
    }
    private void updateHardware(){
        rpm = Kv * state.power + (state.power-currentVelocity) * Kp;
        shoot1.setPower(rpm * Voltage);
        shoot2.setPower(rpm * Voltage);

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
    public static double getAngularVelocity(){
        return (currentVelocity/28) * (Math.PI*2) * 2/3;
    }
    public void setState(State state){
        this.state = state;
    }
    public State getState(){
        return state;
    }
}