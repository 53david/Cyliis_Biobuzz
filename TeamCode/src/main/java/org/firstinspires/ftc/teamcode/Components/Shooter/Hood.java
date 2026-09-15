package org.firstinspires.ftc.teamcode.Components.Shooter;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.teamcode.Wrappers.Hardware;
import org.firstinspires.ftc.teamcode.Wrappers.Odo;

@Config
public class Hood {
    ServoImplEx servo;
    public static double k = 0,offset = 0;
    public static double idlePos = 0,shootPos = 0, pos = 0.19;
    public enum State{
        PAUSE(pos),
        IDLE(idlePos),
        SHOOT(shootPos);
        double position;
        State(double position){
            this.position = position;
        }
    }
    public State state = State.IDLE;
    public Hood(){
        servo = Hardware.ssh5;
        servo.setDirection(Servo.Direction.FORWARD);
    }
    public void update(){
        updateState();
        updatePosition();
        updateHardware();
    }
    private void updateHardware(){
        servo.setPosition(state.position + offset);
    }
    private void updateState(){
        switch (state){
            case PAUSE:
                break;
            case IDLE:
                idlePos = 0;
            case SHOOT:
                shootPos = 0;
                break;
        }
    }
    private void updatePosition(){
        State.IDLE.position = idlePos;
        State.SHOOT.position = shootPos;
    }
    public void setState(State state){
        this.state = state;
    }
    public State getState(){
        return state;
    }
    public double getPosition(){
        return state.position;
    }
    public double calculateHoodPos(double distance){
        return 0;
    }

}
