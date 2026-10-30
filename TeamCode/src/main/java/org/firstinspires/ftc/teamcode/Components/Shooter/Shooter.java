package org.firstinspires.ftc.teamcode.Components.Shooter;

import static org.firstinspires.ftc.teamcode.Wrappers.Hardware.gm1;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Components.Intake.Pitch;
import org.firstinspires.ftc.teamcode.Math.ShooterCalculator;

public class Shooter {
    ShooterCalculator shooterCalculator;
    public Flywheel flyWheel;
    ElapsedTime timer;
    public Hood hood;
    public Turret turret;

    public enum State {
        IDLE,
        SHOOT,
        ACTIVE,
    }

    public State state;

    public Shooter(State state) {
        timer = new ElapsedTime();
        timer.startTime();
        this.state = state;
        turret = new Turret();
        flyWheel = new Flywheel();
        shooterCalculator = new ShooterCalculator();
        hood = new Hood();
    }

    public void updateState() {
        switch (state) {
            case IDLE:
                flyWheel.setState(Flywheel.State.IDLE);
                hood.setState(Hood.State.PAUSE);
                timer.reset();
                break;
            case ACTIVE:
                flyWheel.setState(Flywheel.State.IDLE);
                hood.setState(Hood.State.IDLE);
                timer.reset();
                break;
            case SHOOT:
                flyWheel.setState(Flywheel.State.IDLE);
                hood.setState(Hood.State.SHOOT);
                if (Pitch.state != Pitch.State.SHOOT) timer.reset();
                if (timer.seconds() > 1.25 && !gm1.a) state = State.ACTIVE;
                break;
        }

    }

    public void update() {
        shooterCalculator.update();
        updateState();
        hood.update();
        flyWheel.update();
        turret.update();
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;

    }
}