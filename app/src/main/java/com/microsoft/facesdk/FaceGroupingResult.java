package com.microsoft.facesdk;

/**
 * Created by chaowa on 7/12/2016.
 */
public class FaceGroupingResult {
    /**
     * group result, each id represents the group which feature in corresponding position belongs to. Group id could be -1, which represents this feature does not belong to any other group.
     */
    public int[] groupsId;
}
