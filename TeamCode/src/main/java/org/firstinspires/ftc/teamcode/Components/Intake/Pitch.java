package org.firstinspires.ftc.teamcode.Components.Intake;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Math.BetterMotionProfile;
import org.firstinspires.ftc.teamcode.Wrappers.Hardware;

@Config
public class Pitch {
    Servo servo;
    public static double idlePos = 0.285, transferPos = 0.505,acc = 20, maxVel = 20,dec = 20,currentPos;
    BetterMotionProfile profile;
    public enum State{
        IDLE(idlePos),
        SHOOT(transferPos),
        GOING_IDLE(idlePos,IDLE),
        GOING_SHOOT(transferPos,SHOOT);
        double position; State nextState;
        State(double position){
            this.position = position;
            this.nextState = this;
        }
        State(double position, State nextState){
            this.position = position;
            this.nextState = nextState;
        }
    }
    public static State state;
    public Pitch(){
        state = State.IDLE;
        servo = Hardware.ssh0;
        profile = new BetterMotionProfile(maxVel,acc,dec);
        profile.setMotion(idlePos , idlePos , 0);

    }
    public void update(){
        updateState();
        updatePositions();
        updateHardware();
    }
    public void updateState(){
        switch (state){
            case GOING_SHOOT:
            case GOING_IDLE:
                if (profile.getPosition() == profile.finalPosition) state = state.nextState;
                break;
        }
    }
    public void updatePositions(){
        State.IDLE.position = idlePos;
        State.SHOOT.position = transferPos;
        State.GOING_IDLE.position = idlePos;
        State.GOING_SHOOT.position = transferPos;
    }
    public void updateHardware(){
        currentPos = profile.getPosition();
        profile.update();
        if(profile.finalPosition != state.position) profile.setMotion(currentPos, state.position, profile.getVelocity());
        servo.setPosition(profile.getPosition());
    }
    public void setState(State state){
        this.state = state;
    }
    public State getState(){
        return state;
    }
    public static boolean isOptimal(){
        return Math.abs(state.position-currentPos) == 0;
    }
}
