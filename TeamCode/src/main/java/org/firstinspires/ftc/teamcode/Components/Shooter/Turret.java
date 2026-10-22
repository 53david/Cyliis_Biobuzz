package org.firstinspires.ftc.teamcode.Components.Shooter;

import static org.firstinspires.ftc.teamcode.Wrappers.Hardware.pp;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Wrappers.Hardware;
import org.firstinspires.ftc.teamcode.Wrappers.Odo;

@Config
public class Turret {
    ServoImplEx servo1,servo2;
    double x = 0;
    public static double minAngleLimit = 0.05, maxAngleLimit = 0.95;
    public static double redX = 0,redY = -840;
    public static double blueX = -1330,blueY = 435;
    public static double offsetUp = 0, offsetDown = -600;
    public double targetAngle = 0;
    public static double a = 0;
    public double targetPosition = 0.5;
    public boolean pause = false;
    public double maxAngle = Math.PI*2 + Math.PI/6;
    public static double offset = 0;
    public static double offsetX = 0,offsetY = 0;
    public static double dx = 0, tx =0;
    public static double dy = 0, ty = 0;
    public enum HiveState{
        UP(offsetUp),
        DOWN(offsetDown);
        double offset;
        HiveState(double offset){
            this.offset = offset;
        }
    }
    public enum State{
        RED(redX,redY),
        BLUE(blueX,blueY),
        IDLE;
        double x,y;
        State(){

        }
        State(double x,double y){
            this.x = x;
            this.y = y;
        }
    }
    public State state = State.BLUE;
    public HiveState hiveState = HiveState.UP;
    public Turret() {
        servo1 = Hardware.ssh1;
        servo2 = Hardware.ssh2;
        servo1.setPwmRange(new PwmControl.PwmRange(500, 2500));
        servo2.setPwmRange(new PwmControl.PwmRange(500, 2500));

        servo1.setDirection(Servo.Direction.REVERSE);
        servo2.setDirection(Servo.Direction.REVERSE);

    }
    public void update() {
        updateTargetPos();
        updateAngle();
        updateServosPosition();
        updateState();

    }

    private void updateServosPosition() {
        if (pause){
            servo1.setPosition(0.5);
            servo2.setPosition(0.5);
            return;
        }
        double rAngle = targetAngle + offset;
        rAngle -= Odo.heading;
        rAngle = normalizeRadians(rAngle);
        rAngle = rAngle / maxAngle;

        targetPosition = rAngle;
        targetPosition = Math.max(minAngleLimit, targetPosition);
        targetPosition = Math.min(maxAngleLimit, targetPosition);

        servo1.setPosition(targetPosition);
        servo2.setPosition(targetPosition);

    }

    private void updateAngle() {

        dx = (state.x + offsetX + hiveState.offset) - (Odo.x +Odo.offsetX + Odo.xVelocity * a + tx * Math.cos(Odo.getHeading()));
        dy = (state.y + offsetY) - (Odo.y +Odo.offsetY + Odo.yVelocity * a +tx * Math.sin(Odo.getHeading()));
        targetAngle = Math.atan2(dy, dx);

    }
    public void updateState(){
        switch (state){
            case IDLE:
                pause = true;
                break;
            case BLUE:
            case RED:
                pause = false;
                break;
        }
        if (Odo.downHiveDistance>Odo.upHiveDistance) hiveState=HiveState.UP;
        else hiveState = HiveState.DOWN;
    }
    public void updateTargetPos(){
        State.RED.x = redX; State.RED.y = redY;
        State.BLUE.x = blueX; State.BLUE.y = blueY;
        HiveState.UP.offset = offsetUp;
        HiveState.DOWN.offset = offsetDown;
        Odo.targetX = state.x;
        Odo.targetY = state.y;
    }
    private double normalizeRadians(double angle) {
        angle %= (2.0 * Math.PI);
        if (angle < 0) angle += (2.0 * Math.PI);
        return angle;

    }
    public void setState(State state){
        this.state = state;
    }
    public State getState(){
        return state;
    }
    public HiveState getHiveState(){
        return hiveState;
    }
}