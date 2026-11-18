package org.firstinspires.ftc.teamcode.Components.Intake;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Wrappers.Hardware;

@Config
public class ActiveIntake {
    boolean stall = true;
    public DcMotorEx motor;
    public double current = 0,prevCurrent = 0;
    public static double intakePower = 1, reversePower = -0.55, shootPower = 0.65, idlePower = 0;
    public enum State{
        INTAKE(intakePower),
        REVERSE(reversePower),
        IDLE(idlePower),
        SHOOT(shootPower);
        double power;
        State(double power){
            this.power = power;
        }
        State (){
            this.power = 0;
        }
    }
    public State state;
    public ActiveIntake(){
        state = State.IDLE;
        this.motor = Hardware.mch2;
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void update(){
        current = motor.getCurrent(CurrentUnit.AMPS);
        stateUpdate();
        updatePower();
        updateHardware();
        prevCurrent = current;
    }
    private void stateUpdate(){
        switch (state){
            case IDLE:
            case SHOOT:
            case REVERSE:
                break;
            case INTAKE:
                if (stall) state = State.REVERSE;
                break;
        }
    }
    private void updatePower(){
        State.INTAKE.power = intakePower;
        State.REVERSE.power = reversePower;
        State.IDLE.power = idlePower;
        State.SHOOT.power = shootPower;
    }
    private void updateHardware(){
        if (current>8) stall = true;
        if (current<8 && prevCurrent<6.2) stall = false;
        motor.setPower(state.power);
    }
    public void setState(State state){
        this.state = state;
    }
    public double getCurrent(){
        return current;
    }
}
