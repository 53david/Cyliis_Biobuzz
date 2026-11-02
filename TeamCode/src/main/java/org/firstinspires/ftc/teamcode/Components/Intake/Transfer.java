package org.firstinspires.ftc.teamcode.Components.Intake;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Wrappers.Hardware;

@Config
public class Transfer {
    DcMotorEx motor;
    public static double intakePower = 0.72, reversePower = -1, shootPower = 0.5, idlePower = 0;
    public static double Ks = 0;
    public enum State{
        ACTIVE(intakePower),
        REVERSE(reversePower),
        IDLE,
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
    public Transfer(){
        state = State.IDLE;
        this.motor = Hardware.mch1;
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void update(){
        stateUpdate();
        updatePower();
        updateHardware();
    }
    private void stateUpdate(){
        switch (state){
            case IDLE:
            case SHOOT:
            case ACTIVE:
            case REVERSE:
                break;
        }
    }
    private void updatePower(){
        State.ACTIVE.power = intakePower;
        State.REVERSE.power = reversePower;
        State.IDLE.power = idlePower;
        State.SHOOT.power = shootPower;
    }
    private void updateHardware(){
        motor.setPower(state.power + Ks);
    }
    public void setState(State state){
        this.state = state;
    }
}
