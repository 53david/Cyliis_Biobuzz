package org.firstinspires.ftc.teamcode.Components.Intake;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Math.BetterMotionProfile;
import org.firstinspires.ftc.teamcode.Wrappers.Hardware;
@Config
public class FlowerRamp {
    public static double intakePos= 0.5, cameraPos = 0, idlePos = 0.2, acc =20,dec = 20,maxVel = 20;
    BetterMotionProfile profile;
    Servo servo;

    public enum State{
        UP(idlePos),
        DOWN(intakePos),
        CAMERA_POS(cameraPos),
        GOING_CAMERA_POS(cameraPos,CAMERA_POS),
        GOING_UP(idlePos,UP),
        GOING_DOWN(intakePos,DOWN);
        State nextState;
        double position;
        State(double position,State nextState){
            this.position = position;
            this.nextState = nextState;
        }
        State(double position){
            this.position = position;
            this.nextState = this;
        }
    }
    State state;
    public FlowerRamp(){
        servo = Hardware.ssh5;
        profile = new BetterMotionProfile(maxVel,acc,dec);
    }
    public void update(){
        updateState();
        updatePosition();
        updateHardware();
    }
    public void updateState(){
        switch (state){
            case GOING_UP:
            case GOING_DOWN:
                if (profile.getPosition() == state.position) state = state.nextState;
                break;
        }
    }
    public void updateHardware(){
        profile.update();
        if(profile.finalPosition != state.position)
            profile.setMotion(profile.getPosition(), state.position, profile.getVelocity());
        servo.setPosition(profile.getPosition());
    }
    public void updatePosition(){
        State.UP.position = idlePos;
        State.DOWN.position = intakePos;
        State.GOING_UP.position = idlePos;
        State.GOING_DOWN.position = intakePos;
    }
    public void setState(State state){
        this.state = state;
    }
    public State getState(){
        return state;
    }
}
