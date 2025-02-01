package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Pose2d;
import com.arcrobotics.ftclib.util.Timing;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunning.MecanumDrive;

import java.util.concurrent.TimeUnit;

@Autonomous
public class SpecimenAuto extends LinearOpMode{
    private MecanumDrive drive;
    private void sleepTools(int ms) {
        Timing.Timer timer = new Timing.Timer(ms, TimeUnit.MILLISECONDS);
        timer.start();
        while (!timer.done() && !isStopRequested()){


        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        drive = new MecanumDrive(hardwareMap, new Pose2d(0,0,0));

    }
}
