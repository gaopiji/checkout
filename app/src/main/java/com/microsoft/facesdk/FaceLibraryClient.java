package com.microsoft.facesdk;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Pair;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.contract.*;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.rest.ClientException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by chaowa on 7/14/2016.
 * A face service client implements with native library.
 */
public class FaceLibraryClient implements FaceServiceClient {
    private FaceDetectionJDA detector;
    public FaceDetectionJDA getDetector(){ return detector; }
    private FaceRecognitionCNN recognizer;
    public FaceRecognitionCNN getRecognizer(){ return recognizer; }
    private FaceLibraryClient(Context c){
        byte[] bytes = null;
        try {
            InputStream v = c.getAssets().open("Detection.001/model.mdl");
            bytes = Utils.getBytes(v);
        }
        catch(Exception ex)
        {
        }

        try
        {
            detector = FaceDetectionJDA.create(bytes);
        }
        catch (Exception ex){
        }

        byte[] recognitionBytes = null;
        try {
            InputStream recognitionModel = c.getAssets().open("Recognition.003/model.mdl");
            recognitionBytes = Utils.getBytes(recognitionModel);
        }
        catch (Exception ex) {

        }
        try {
            recognizer = FaceRecognitionCNN.create(recognitionBytes);
        }
        catch (Exception ex) {
        }
    }
    private static FaceLibraryClient client;
    public static FaceLibraryClient getClient(Context c){
        if (client == null) {
            client = new FaceLibraryClient(c);
        }
        return client;
    }
    private static HashMap<String, PersonGroup> personGroups = new HashMap<>();
    private static HashMap<String, com.microsoft.facesdk.Face> tempFaces = new HashMap<>();

    @Override
    public Face[] detect(String s, boolean b, boolean b1, FaceAttributeType[] faceAttributeTypes) throws ClientException, IOException {
        throw new ClientException("Not impl");
    }

    @Override
    public Face[] detect(InputStream inputStream, boolean b, boolean b1, FaceAttributeType[] faceAttributeTypes) throws ClientException, IOException {
        FaceDetectionResult[] rects = null;
        Image img = null;
        try {
            img = Image.loadImageFromStreamAsARGB(inputStream);
            Image grayImg = img.toGrayscale();
            rects = client.getDetector().detectAndAlign(grayImg);
        }
        catch (Exception ex) {
        }
        // Start detection.
        Face[] faces = new Face[rects.length];
        for(int i=0; i < rects.length; i++){
            faces[i] = new Face();
            faces[i].faceRectangle = rects[i].getRectangle();
            faces[i].faceLandmarks = rects[i].getLandmarks();
            faces[i].faceAttributes = new FaceAttribute();
            faces[i].faceAttributes.headPose = new HeadPose();
            faces[i].faceAttributes.headPose.roll = 0.0;
            faces[i].faceAttributes.headPose.yaw = 0.0;
            faces[i].faceAttributes.headPose.pitch = 0.0;
            faces[i].faceAttributes.facialHair = new FacialHair();
            faces[i].faceAttributes.facialHair.beard = 0.0;
            faces[i].faceAttributes.facialHair.moustache= 0.0;
            faces[i].faceAttributes.facialHair.sideburns= 0.0;
            faces[i].faceAttributes.glasses = Glasses.NoGlasses;
            faces[i].faceAttributes.gender = "unknown";
            faces[i].faceAttributes.age = 0.0;
            faces[i].faceAttributes.smile = 0.0;
            byte[] feature = null;
            try {
                feature = recognizer.extract(faces[i].faceLandmarks, img);
            }
            catch (Exception ex){}
            UUID id = UUID.randomUUID();
            faces[i].faceId = id;
            com.microsoft.facesdk.Face f = new com.microsoft.facesdk.Face();
            f.id = id.toString();
            f.feature = feature;
            tempFaces.put(id.toString(), f);
        }
        return faces;
    }

    @Override
    public VerifyResult verify(UUID uuid, UUID uuid1) throws ClientException, IOException {
        float distance = recognizer.distance(tempFaces.get(uuid.toString()).feature, tempFaces.get(uuid1.toString()).feature);
        VerifyResult res = new VerifyResult();
        res.confidence = 1.0f - distance;
        res.isIdentical = res.confidence > 0.5f;
        return res;
    }

    @Override
    public IdentifyResult[] identity(String s, UUID[] uuids, int i) throws ClientException, IOException {
        ArrayList<IdentifyResult> res = new ArrayList<>();
        for (UUID uuid: uuids) {
            PersonIdentified[] ps = recognizer.identify(tempFaces.get(uuid.toString()).feature, personGroups.get(s), i);
            IdentifyResult r = new IdentifyResult();
            r.candidates = new ArrayList<>();
            r.faceId = uuid;
            for (PersonIdentified p:ps) {
                Candidate c = new Candidate();
                c.personId = UUID.fromString(p.person.id);
                c.confidence = p.confidence;
                r.candidates.add(c);
            }
            res.add(r);
        }
        return res.toArray(new IdentifyResult[0]);
    }

    @Override
    public void trainPersonGroup(String s) throws ClientException, IOException {
    }

    @Override
    public TrainingStatus getPersonGroupTrainingStatus(String s) throws ClientException, IOException {
        TrainingStatus status = new TrainingStatus();
        status.status = TrainingStatus.Status.Succeeded;
        return status;
    }

    @Override
    public void createPersonGroup(String s, String s1, String s2) throws ClientException, IOException {
        personGroups.put(s, new PersonGroup());
    }

    @Override
    public void deletePersonGroup(String s) throws ClientException, IOException {
        personGroups.remove(s);
    }

    @Override
    public void updatePersonGroup(String s, String s1, String s2) throws ClientException, IOException {
    }

    @Override
    public com.microsoft.projectoxford.face.contract.PersonGroup getPersonGroup(String s) throws ClientException, IOException {
        com.microsoft.projectoxford.face.contract.PersonGroup pg = new com.microsoft.projectoxford.face.contract.PersonGroup();
        pg.personGroupId = s;
        pg.trainingStatus = new TrainingStatus();
        pg.trainingStatus.status = TrainingStatus.Status.Succeeded;
        return pg;
    }

    @Override
    public com.microsoft.projectoxford.face.contract.PersonGroup[] getPersonGroups() throws ClientException, IOException {
        ArrayList<com.microsoft.projectoxford.face.contract.PersonGroup> pgs = new ArrayList<>();
        for (PersonGroup pg: personGroups.values()) {
            com.microsoft.projectoxford.face.contract.PersonGroup p = new com.microsoft.projectoxford.face.contract.PersonGroup();
            p.personGroupId = pg.id;
            p.trainingStatus = new TrainingStatus();
            p.trainingStatus.status = TrainingStatus.Status.Succeeded;
            pgs.add(p);
        }
        return pgs.toArray(new com.microsoft.projectoxford.face.contract.PersonGroup[0]);
    }

    @Override
    public CreatePersonResult createPerson(String s, String s1, String s2) throws ClientException, IOException {
        Person p = new Person();
        UUID id = UUID.randomUUID();
        p.id = id.toString();

        personGroups.get(s).persons.put(id.toString(), p);
        CreatePersonResult res = new CreatePersonResult();
        res.personId = id;
        return res;
    }

    @Override
    public com.microsoft.projectoxford.face.contract.Person getPerson(String s, UUID uuid) throws ClientException, IOException {
        Person p = personGroups.get(s).persons.get(uuid.toString());
        com.microsoft.projectoxford.face.contract.Person resp = new com.microsoft.projectoxford.face.contract.Person();
        resp.personId = uuid;
        ArrayList<UUID> faceIds = new ArrayList<>();
        for (com.microsoft.facesdk.Face f:p.features.values()) {
            faceIds.add(UUID.fromString(f.id));
        }
        resp.faceIds = faceIds.toArray(new UUID[0]);
        return resp;
    }

    @Override
    public com.microsoft.projectoxford.face.contract.Person[] getPersons(String s) throws ClientException, IOException {
        ArrayList<com.microsoft.projectoxford.face.contract.Person> ps = new ArrayList<>();
        for (Person p: personGroups.get(s).persons.values()) {
            com.microsoft.projectoxford.face.contract.Person resp = new com.microsoft.projectoxford.face.contract.Person();
            resp.personId = UUID.fromString(p.id);
            ArrayList<UUID> faceIds = new ArrayList<>();
            for (com.microsoft.facesdk.Face f:p.features.values()) {
                faceIds.add(UUID.fromString(f.id));
            }
            resp.faceIds = faceIds.toArray(new UUID[0]);
            ps.add(resp);
        }
        return ps.toArray(new com.microsoft.projectoxford.face.contract.Person[0]);
    }

    @Override
    public AddPersistedFaceResult addPersonFace(String s, UUID uuid, String s1, String s2, FaceRectangle faceRectangle) throws ClientException, IOException {
        throw new ClientException("Not impl");
    }

    @Override
    public AddPersistedFaceResult addPersonFace(String s, UUID uuid, InputStream inputStream, String s1, FaceRectangle faceRectangle) throws ClientException, IOException {
        FaceDetectionResult[] rects = null;
        Image img = null;
        try {
            img = Image.loadImageFromStreamAsARGB(inputStream);
            Image grayImg = img.toGrayscale();
            rects = client.getDetector().detectAndAlign(grayImg);
        }
        catch (Exception ex) {
        }
        // Start detection.
        for(int i=0; i < rects.length; i++){
            FaceRectangle rect = rects[i].getRectangle();
            if (rect.left == faceRectangle.left && rect.top == faceRectangle.top)
            {
                byte[] feature = null;
                try {
                    feature = recognizer.extract(rects[i].getLandmarks(), img);
                }
                catch (Exception ex){}
                UUID id = UUID.randomUUID();
                com.microsoft.facesdk.Face f = new com.microsoft.facesdk.Face();
                f.id = id.toString();
                f.feature = feature;
                personGroups.get(s).persons.get(uuid.toString()).features.put(id.toString(), f);
                AddPersistedFaceResult res = new AddPersistedFaceResult();
                res.persistedFaceId = id;
                return res;
            }
        }
        return null;
    }

    @Override
    public PersonFace getPersonFace(String s, UUID uuid, UUID uuid1) throws ClientException, IOException {
        com.microsoft.facesdk.Face f = personGroups.get(s).persons.get(uuid.toString()).features.get(uuid1.toString());
        PersonFace pf = new PersonFace();
        pf.persistedFaceId = UUID.fromString(f.id);
        return pf;
    }

    @Override
    public void updatePersonFace(String s, UUID uuid, UUID uuid1, String s1) throws ClientException, IOException {
        return;
    }

    @Override
    public void updatePerson(String s, UUID uuid, String s1, String s2) throws ClientException, IOException {
        return;
    }

    @Override
    public void deletePerson(String s, UUID uuid) throws ClientException, IOException {
        personGroups.get(s).persons.remove(uuid.toString());
        return;
    }

    @Override
    public void deletePersonFace(String s, UUID uuid, UUID uuid1) throws ClientException, IOException {
        personGroups.get(s).persons.get(uuid.toString()).features.remove(uuid1.toString());
        return;
    }

    @Override
    public SimilarFace[] findSimilar(UUID u, UUID[] uuids, int i) throws ClientException, IOException {
        byte[] feature = tempFaces.get(u.toString()).feature;
        ArrayList<byte[]> features = new ArrayList<>();
        for (UUID uuid: uuids) {
            features.add(tempFaces.get(uuid.toString()).feature);
        }
        ArrayList<Pair<Integer, Float>> res = recognizer.findSimilar(feature, features, i);
        SimilarFace[] r = new SimilarFace[res.size()];
        int rid = 0;
        for (Pair<Integer, Float> p: res) {
            r[rid] = new SimilarFace();
            r[rid].faceId = uuids[p.first];
            r[rid].confidence = p.second;
        }
        return r;
    }

    @Override
    public SimilarPersistedFace[] findSimilar(UUID uuid, String s, int i) throws ClientException, IOException {
        throw new ClientException("Not impl");
    }

    @Override
    public GroupResult group(UUID[] uuids) throws ClientException, IOException {
        ArrayList<byte[]> features = new ArrayList<>();
        for (UUID u:uuids) {
            features.add(tempFaces.get(u.toString()).feature);
        }
        FaceGroupingResult groupIndex = recognizer.group(features);
        GroupResult gr = new GroupResult();
        HashMap<Integer, ArrayList<UUID>> g = new HashMap<>();
        ArrayList<UUID> messyGroup = new ArrayList<>();
        int index = 0;
        for (int idx:groupIndex.groupsId) {
            if (idx >= 0){
                if (!g.containsKey(idx)){
                    g.put(idx, new ArrayList<UUID>());
                }
                g.get(idx).add(uuids[index]);
            }
            else{
                messyGroup.add(uuids[index]);
            }
            index++;
        }
        gr.groups = new ArrayList<>();
        for(ArrayList<UUID> u: g.values()){
            gr.groups.add(u.toArray(new UUID[0]));
        }
        gr.messyGroup = messyGroup;
        return gr;
    }

    @Override
    public void createFaceList(String s, String s1, String s2) throws ClientException, IOException {
        throw new ClientException("Not impl");
    }

    @Override
    public FaceList getFaceList(String s) throws ClientException, IOException {
        throw new ClientException("Not impl");
    }

    @Override
    public FaceListMetadata[] listFaceLists() throws ClientException, IOException {
        throw new ClientException("Not impl");
    }

    @Override
    public void updateFaceList(String s, String s1, String s2) throws ClientException, IOException {
        throw new ClientException("Not impl");
    }

    @Override
    public void deleteFaceList(String s) throws ClientException, IOException {
        throw new ClientException("Not impl");
    }

    @Override
    public AddPersistedFaceResult addFacesToFaceList(String s, String s1, String s2, FaceRectangle faceRectangle) throws ClientException, IOException {
        throw new ClientException("Not impl");
    }

    @Override
    public AddPersistedFaceResult AddFaceToFaceList(String s, InputStream inputStream, String s1, FaceRectangle faceRectangle) throws ClientException, IOException {
        throw new ClientException("Not impl");
    }

    @Override
    public void deleteFacesFromFaceList(String s, UUID uuid) throws ClientException, IOException {
        throw new ClientException("Not impl");
    }
}
