package org.firstinspires.ftc.teamcode.OctoQuad;

import com.qualcomm.hardware.digitalchickenlabs.OctoQuad;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

@TeleOp
public class Test extends LinearOpMode {
    OctoQuad octoQuad;
    public void runOpMode(){
        octoQuad = hardwareMap.get(OctoQuad.class,"octoQuad");
        while (opModeIsActive()){
            telemetry.addData("X",octoQuad.readLocalizerData().posX_mm);
        }
    }
}
