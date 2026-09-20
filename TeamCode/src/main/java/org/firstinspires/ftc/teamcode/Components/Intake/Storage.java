package org.firstinspires.ftc.teamcode.Components.Intake;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Wrappers.Hardware;

public class Storage {
    DigitalChannel sensor1,sensor2,sensor3,sensor4;
    boolean slot1,slot2,slot3,slot4;
    public enum State{
        READY_FOR_TRANSFER,
        NO_POLLEN,
        WAITING_FOR_POLLEN,
    };
    State state;
    public Storage (){
        state = State.NO_POLLEN;
        sensor1 = Hardware.bb1;
        sensor2 = Hardware.bb2;
        sensor3 = Hardware.bb3;
        sensor4 = Hardware.bb4;

    }
    public void update(){
        slot1 = getStateSensor1();
        slot2 = getStateSensor2();
        slot3 = getStateSensor3();
        slot4 = getStateSensor4();

        if (slot1 && slot2 && slot3 && slot4) {setState(State.READY_FOR_TRANSFER);}
        else if (slot1 || slot2 || slot3 || slot4) {setState(State.WAITING_FOR_POLLEN);}
        else setState(State.NO_POLLEN);
    }
    public void setState(State state){
        this.state = state;
    }
    public boolean getStateSensor1(){
        return !sensor1.getState();
    }
    public boolean getStateSensor2(){
        return !sensor2.getState();
    }
    public boolean getStateSensor3(){
        return !sensor3.getState();
    }
    public boolean getStateSensor4(){
        return !sensor4.getState();
    }
    public State getState(){
        return state;
    }
}
