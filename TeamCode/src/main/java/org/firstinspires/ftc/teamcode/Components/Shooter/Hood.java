package org.firstinspires.ftc.teamcode.Components.Shooter;


import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Math.ShooterCalculator;
import org.firstinspires.ftc.teamcode.Wrappers.Hardware;

@Config
public class Hood {
    ServoImplEx servo;
    public static double k = -0.00003,offset = 0;
    public static double x = 0;
    public static double idlePos = 0.35,shootPos = 0, pos = 0.42,maxPos = 0.72;
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
        servo = Hardware.ssh1;
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
                idlePos = Range.clip(ShooterCalculator.getHoodAngle(),0.335,0.575);
            case SHOOT:
                shootPos = Range.clip(ShooterCalculator.getHoodAngle() - k*(Flywheel.currentVelocity- Flywheel.targetVelocity),0.35,0.575);
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

}
