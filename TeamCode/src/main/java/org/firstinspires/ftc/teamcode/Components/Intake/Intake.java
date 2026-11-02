package org.firstinspires.ftc.teamcode.Components.Intake;

import static org.firstinspires.ftc.teamcode.Wrappers.Hardware.gm1;

import com.qualcomm.robotcore.util.ElapsedTime;

public class Intake {
    ElapsedTime timer;
    public ActiveIntake activeIntake;
    public Transfer transfer;
    public Pitch pitch;
    public Storage storage;
    public Ramp ramp;
    public enum State{
        IDLE,
        REVERSE,
        INTAKE,
        SHOOT(IDLE);
        State nextState;
        State(State nextState){
            this.nextState = nextState;
        }
        State(){
            this.nextState = this;
        }
    }
    public State state;
    public static State state1;
    public Intake(){
        timer = new ElapsedTime();
        state = State.IDLE;
        activeIntake = new ActiveIntake();
        transfer = new Transfer();
        pitch = new Pitch();
        storage = new Storage();
        ramp = new Ramp();
    }
    public void update(){
        stateUpdate();
        activeIntake.update();
        transfer.update();
        pitch.update();
        storage.update();
        ramp.update();

        if (state != State.SHOOT) timer.reset();
    }
    public void stateUpdate(){
        state1 = state;
        switch (state){
            case IDLE:
                activeIntake.setState(ActiveIntake.State.IDLE);
                transfer.setState(Transfer.State.IDLE);
                pitch.setState(Pitch.State.IDLE);
                break;
            case INTAKE:
                activeIntake.setState(ActiveIntake.State.INTAKE);
                transfer.setState(Transfer.State.ACTIVE);
                pitch.setState(Pitch.State.IDLE);
                break;
            case REVERSE:
                activeIntake.setState(ActiveIntake.State.REVERSE);
                transfer.setState(Transfer.State.IDLE);
                pitch.setState(Pitch.State.IDLE);
                break;
            case SHOOT:
                if (!Pitch.isOptimal()) {transfer.setState(Transfer.State.IDLE); timer.reset();}
                else if (Pitch.isOptimal()) {transfer.setState(Transfer.State.SHOOT);}
                if (timer.seconds()>0.55 && !gm1.a) state = state.nextState;
                if (pitch.getState() != Pitch.State.GOING_SHOOT && pitch.getState()!= Pitch.State.SHOOT) pitch.setState(Pitch.State.GOING_SHOOT);
                if (gm1.right_bumper)activeIntake.setState(ActiveIntake.State.SHOOT);
                else activeIntake.setState(ActiveIntake.State.IDLE);
                break;
        }
    }
    public void setState(State state){
        this.state = state;
    }
    public State getState(){
        return state;
    }
    public void deployRamp(){
        if (ramp.getState() != Ramp.State.GOING_DOWN && ramp.getState() != Ramp.State.DOWN)
            ramp.setState(Ramp.State.GOING_DOWN);

    }
    public void retractRamp(){
        if (ramp.getState() != Ramp.State.GOING_UP && ramp.getState() != Ramp.State.UP)
            ramp.setState(Ramp.State.GOING_UP);

    }
}
