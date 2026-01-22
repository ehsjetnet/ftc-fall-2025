package org.firstinspires.ftc.teamcode.layer;

import java.util.iterator;

import org.firstinspires.ftc.teamcode.layer.Layer;
import org.firstinspires.ftc.teamcode.layer.LayerSetupInfo;
import org.firstinspires.ftc.teamcode.task.Task;

public class VisionProcessingLayer implements Layer{
    @Override 
    public void setup(LayerSetupInfo setupInfo) {

    }

    @Override
    public boolean isTaskDone() {}

    @Override
    public Iterator<Task> update(Iterable<Task> completed) {
       /* 
       Essentially what will be passed down to the lower layers. E.g. a task containing meta data such as
       distance, angle, height, etc retrieved from the april tag.
       */
    }

    @Override
    public acceptTask(Task task) {
        /* place where tasks are received from a higher layer like GamepadInputGenerator during teleop or 
        win layer during autonomous, and processed in this layer.
        
        TLDR: Basically maps a task from a higher layer into something this layer has to do*/
    }

}
