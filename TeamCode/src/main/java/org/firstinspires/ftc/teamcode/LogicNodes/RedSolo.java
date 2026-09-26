package org.firstinspires.ftc.teamcode.LogicNodes;

import static org.firstinspires.ftc.teamcode.Trajectories.RedSoloTrajectory.edgePos;
import static org.firstinspires.ftc.teamcode.Trajectories.RedSoloTrajectory.flower1Pos;
import static org.firstinspires.ftc.teamcode.Trajectories.RedSoloTrajectory.flower2Pos;
import static org.firstinspires.ftc.teamcode.Trajectories.RedSoloTrajectory.gardenPos;
import static org.firstinspires.ftc.teamcode.Trajectories.RedSoloTrajectory.goingFlower1Pos;
import static org.firstinspires.ftc.teamcode.Trajectories.RedSoloTrajectory.goingFlower2Pos;
import static org.firstinspires.ftc.teamcode.Trajectories.RedSoloTrajectory.goingGardenPos;
import static org.firstinspires.ftc.teamcode.Trajectories.RedSoloTrajectory.goingLoadingZonePos;
import static org.firstinspires.ftc.teamcode.Trajectories.RedSoloTrajectory.loadingZonePos;
import static org.firstinspires.ftc.teamcode.Trajectories.RedSoloTrajectory.parkPos;
import static org.firstinspires.ftc.teamcode.Trajectories.RedSoloTrajectory.shootPos;
import static org.firstinspires.ftc.teamcode.Trajectories.RedSoloTrajectory.trussPos;
import static org.firstinspires.ftc.teamcode.Trajectories.RedSoloTrajectory.underHivePos;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Components.Chassis.Chassis;
import org.firstinspires.ftc.teamcode.Components.Intake.FlowerRamp;
import org.firstinspires.ftc.teamcode.Components.Intake.Intake;
import org.firstinspires.ftc.teamcode.Components.Intake.Storage;
import org.firstinspires.ftc.teamcode.Components.Shooter.Shooter;
import org.firstinspires.ftc.teamcode.Robot.Node;
import org.firstinspires.ftc.teamcode.Wrappers.Hardware;
import org.firstinspires.ftc.teamcode.Wrappers.Odo;

public class RedSolo {
    ElapsedTime timer, globalTimer;
    Chassis chassis;
    Intake intake;
    Shooter shooter;
    Odo odo;
    Node shoot, goingTruss, goingEdge, goingFlower1, goingFlower2, goingGarden, goingLoadingZone, flower1, flower2, garden, loadingZone, hive, park;
    public Node currentNode;

    public RedSolo(HardwareMap hardwareMap) {
        Hardware.init(hardwareMap);
        timer = new ElapsedTime();
        globalTimer = new ElapsedTime();
        timer.startTime();
        globalTimer.startTime();
        odo = new Odo();
        chassis = new Chassis(Chassis.State.PID);
        intake = new Intake();
        shooter = new Shooter(Shooter.State.ACTIVE);

        shoot = new Node("shoot");
        goingTruss = new Node("goingTruss");
        goingEdge = new Node("goingEdge");
        goingFlower1 = new Node("goingFlower1");
        goingFlower2 = new Node("goingFlower2");
        goingGarden = new Node("goingGarden");
        goingLoadingZone = new Node("goingLoadingZone");
        flower1 = new Node("flower1");
        flower2 = new Node("flower2");
        garden = new Node("garden");
        loadingZone = new Node("loadingZone");
        hive = new Node("hive");
        park = new Node("park");
        currentNode = shoot;

        shoot.addConditions(
                () -> {
                    chassis.setTargetPosition(shootPos[(shoot.index+1)/2]);
                    if (intake.storage.getState() == Storage.State.READY_FOR_TRANSFER && intake.getState()!= Intake.State.SHOOT) {intake.setState(Intake.State.REVERSE);}
                    else if (intake.storage.getState() == Storage.State.WAITING_FOR_POLLEN) {intake.setState(Intake.State.IDLE);}
                    if (chassis.inPosition(120, 120, 0.1)) {intake.setState(Intake.State.SHOOT);shooter.setState(Shooter.State.SHOOT);}
                },
                () -> {
                    return intake.storage.getState() == Storage.State.NO_POLLEN;
                },
                new Node[]{goingGarden,goingFlower2,goingFlower1}
        );
        goingGarden.addConditions(
                ()->{
                    chassis.setTargetPosition(goingGardenPos,false);
                    intake.setState(Intake.State.INTAKE);
                },
                ()->{
                    return chassis.inPosition(300,300,0.12);
                },
                new Node[]{garden}
        );
        garden.addConditions(
                ()->{
                    chassis.setTargetPosition(gardenPos);
                    intake.setState(Intake.State.INTAKE);
                    if (chassis.inPosition(110,110,0.1))chassis.setLowPowerMode(true);
                },
                ()->{
                    return chassis.inPosition(60,60,0.1);
                },
                new Node[]{shoot}

        );
        goingLoadingZone.addConditions(
                ()->{
                    chassis.setTargetPosition(goingLoadingZonePos,false);
                    intake.setState(Intake.State.INTAKE);
                },
                ()->{
                    return chassis.inPosition(300,300,0.1);
                },
                new Node[]{loadingZone}
        );
        loadingZone.addConditions(
                ()->{
                    chassis.setTargetPosition(loadingZonePos);
                    intake.setState(Intake.State.INTAKE);
                    if (chassis.inPosition(70,70,0.1)) chassis.setLowPowerMode(true);
                },
                ()->{
                    return chassis.inPosition(45,45,0.1);
                },
                new Node[]{shoot}

        );
        goingFlower1.addConditions(
                () -> {
                    chassis.setTargetPosition(goingFlower1Pos);
                    if (chassis.inPosition(220,220,0.1)) intake.setState(Intake.State.INTAKE_FROM_FLOWER);
                },
                ()->{
                    return intake.flowerRamp.getState() == FlowerRamp.State.DOWN;
                },
                new Node[]{flower1}
        );
        flower1.addConditions(
                ()->{
                    chassis.setTargetPosition(flower1Pos);
                    intake.setState(Intake.State.INTAKE_FROM_FLOWER);
                    if (!chassis.inPosition(80,80,0.1)) timer.reset();
                    if (timer.seconds()>0.15) chassis.turnOn();
                },
                ()->{
                    return intake.storage.getState()== Storage.State.READY_FOR_TRANSFER;
                },
                new Node[]{shoot}
        );
        goingFlower2.addConditions(
                () -> {
                    chassis.setTargetPosition(goingFlower2Pos,false);
                    if (chassis.inPosition(220,220,0.1)) intake.setState(Intake.State.INTAKE_FROM_FLOWER);
                },
                ()->{
                    return intake.flowerRamp.getState() == FlowerRamp.State.DOWN;
                },
                new Node[]{flower2}
        );
        flower2.addConditions(
                ()->{
                    chassis.setTargetPosition(flower2Pos);
                    intake.setState(Intake.State.INTAKE_FROM_FLOWER);
                    if (!chassis.inPosition(80,80,0.1)) timer.reset();
                    if (timer.seconds()>0.15) chassis.turnOn();
                },
                ()->{
                    return intake.storage.getState()== Storage.State.READY_FOR_TRANSFER;
                },
                new Node[]{shoot}
        );
        goingTruss.addConditions(
                ()->{
                    intake.setState(Intake.State.INTAKE);
                    chassis.setTargetPosition(trussPos);
                },
                ()->{
                    return chassis.inPosition(400,400,0.14);
                },
                new Node[]{hive}
        );
        goingEdge.addConditions(
                ()->{
                    intake.setState(Intake.State.INTAKE);
                    chassis.setTargetPosition(edgePos);
                },
                ()->{
                    return chassis.inPosition(400,400,0.14);
                },
                new Node[]{hive}
        );
        hive.addConditions(
                ()->{
                    intake.setState(Intake.State.INTAKE);
                    chassis.setTargetPosition(underHivePos[(shoot.index+1)%2]);
                },
                ()->{
                    return chassis.inPosition(60,60,0.1);
                },
                new Node[]{shoot}
        );
        park.addConditions(
                ()->{
                    chassis.setTargetPosition(parkPos);
                    intake.setState(Intake.State.IDLE);
                },
                ()->{
                    return chassis.inPosition(30,30,0.1);
                },
                new Node[]{park}
        );

    }
    public void update(){
        currentNode.run();
        chassis.update();
        intake.update();
        shooter.update();
        odo.update();

        if (currentNode != flower1 && currentNode != flower2) chassis.turnOn();
        if (currentNode != garden) chassis.setLowPowerMode(false);
        if (currentNode != shoot && globalTimer.seconds()>29) currentNode = park;
        if (currentNode.transition()){
            currentNode = currentNode.next[Math.min(currentNode.index++, currentNode.next.length-1)];
        }
    }
}

