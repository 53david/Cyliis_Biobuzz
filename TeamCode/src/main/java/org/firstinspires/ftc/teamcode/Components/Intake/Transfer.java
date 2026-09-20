package org.firstinspires.ftc.teamcode.Components.Intake;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.Wrappers.Hardware;

@Config
public class Transfer {
    CRServo motor;
    public static double intakePower = 0.8, reversePower = 0.45, shootPower = 1, idlePower = 0.08;
    public enum State{
        ACTIVE(intakePower),
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
    public Transfer(){
        state = State.IDLE;
        this.motor = Hardware.sch1;
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
        motor.setPower(state.power);
    }
    public void setState(State state){
        this.state = state;
    }
}
