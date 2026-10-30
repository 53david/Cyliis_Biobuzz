package org.firstinspires.ftc.teamcode.Components.Shooter;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Math.LowPassFilter;
import org.firstinspires.ftc.teamcode.Math.ShooterCalculator;
import org.firstinspires.ftc.teamcode.Wrappers.Hardware;

@Config
public class Flywheel {
    DcMotorEx encoder,shoot1,shoot2;
    public static double Kp = 0.00055;
    public static double Ks = 0;
    public static double Kv = 0.000425;
    public static double Ka = 0;
    public static double filterParameter = 1;
    LowPassFilter passFilter = new LowPassFilter(filterParameter,0);
    public static double shootPower = 0,idlePower = 1300;
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
        shoot1 = Hardware.mch2;
        shoot2 = Hardware.mch3;
        shoot1.setDirection(DcMotorSimple.Direction.REVERSE);
        shoot2.setDirection(DcMotorSimple.Direction.FORWARD);
    }
    public void update(){
        targetVelocity = state.power;
        passFilter.setFilterParameter(filterParameter);
        currentVelocity = passFilter.getValue(encoder.getVelocity());
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
                shootPower = ShooterCalculator.getVelocity();
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

}