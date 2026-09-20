package org.firstinspires.ftc.teamcode.Components.Intake;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Wrappers.Hardware;

@Config
public class Pitch {
    Servo servo;
    public static double idlePos = 0, transferPos = 0;
    public enum State{
        IDLE(idlePos),
        SHOOT(transferPos);
        double position;
        State(double position){
            this.position = position;
        }
        State(){

        }
    }
    public State state;
    public Pitch(){
        state = State.IDLE;
        servo = Hardware.ssh0;
    }
    public void update(){
        updateState();
        updatePositions();
        updateHardware();
    }
    public void updateState(){
        switch (state){
            case IDLE:
            case SHOOT:
                break;
        }
    }
    public void updatePositions(){
        State.IDLE.position = idlePos;
        State.SHOOT.position = transferPos;
    }
    public void updateHardware(){
        servo.setPosition(state.position);
    }
    public void setState(State state){
        this.state = state;
    }
}
