package com.tieto.ciweb;

import java.util.LinkedList;
import java.util.List;

import com.tieto.ciweb.api.persistence.PersistenceLayer;
import com.tieto.ciweb.model.Commit;
import com.tieto.ciweb.model.PatchSet;
import com.tieto.ciweb.model.Product;
import com.tieto.ciweb.model.WorkItem;
import com.tieto.ciweb.persistance.InMemoryStorage;

public class Configuration {
	private static Configuration INSTANCE = null;
	
	private final PersistenceLayer persistanceLayer;
	
	public static final Configuration getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new Configuration();
		}
		return INSTANCE;
	}

	private Configuration() {
		persistanceLayer = new InMemoryStorage();
		
		populatePersistanceLayer();
	}
	
	private void populatePersistanceLayer() {
		List<Commit> commits = generateCommits();
		for(Commit commit : commits) {
			//persistanceLayer.store("commits", commit.toJson(), commit.getId());
		}

		persistanceLayer.store("products", new Product("Artifact1","10.0","11.0.5","-").toJson(), "Artifact1");
		persistanceLayer.store("products", new Product("Artifact2","12.0","13.0.16","2f4413ef3a320813646a939f5a672c57338b314c6").toJson(), "Artifact2");
		persistanceLayer.store("products", new Product("Artifact3","8.1","9.0.3","-").toJson(), "Artifact3");
		persistanceLayer.store("products", new Product("Artifact4","1.0","1.1.4","45cc780126f7bf019c27d1b4e6a251c0c5b3298fd").toJson(), "Artifact4");
		persistanceLayer.store("products", new Product("Artifact5","3.0","3.1.1","-").toJson(), "Artifact5");
		persistanceLayer.store("products", new Product("Artifact6","1.0","2.0.4","ee2bdc22b9936161c3e9bf5000f324517542a6f01").toJson(), "Artifact6");
		persistanceLayer.store("products", new Product("Artifact7","5.0","6.0.2","-").toJson(), "Artifact7");
		persistanceLayer.store("products", new Product("Artifact8","7.0","7.0","ee2bdc22b9936161c3e9bf5000f324517542a6f01").toJson(), "Artifact8");
		persistanceLayer.store("products", new Product("Artifact9","3.0","4.0.2","-").toJson(), "Artifact9");

	}

	private List<Commit> generateCommits() {
		List<Commit> commits = new LinkedList<Commit>();
		
		Commit commit1 = new Commit("chid1", "Alice", "user1", "team1", "project1", "master", "10:50");
		commit1.addPatchSet(new PatchSet("chid1-ps1", "10:50").setVerified("-1").setCodeReview("-2"));
		commit1.addPatchSet(new PatchSet("chid1-ps2", "10:55").setVerified("+1").setCodeReview("-2"));
		commit1.addPatchSet(new PatchSet("chid1-ps3", "10:58").setVerified("+1").setCodeReview("-2"));
		commit1.addWorkItem(new WorkItem("JIRA-0001", ""));
		commit1.addWorkItem(new WorkItem("JIRA-0004", ""));
		commit1.addWorkItem(new WorkItem("JIRA-0006", ""));
		
		Commit commit2 = new Commit("chid2", "David", "user4", "team1", "project4", "master", "10:18");
		commit2.addPatchSet(new PatchSet("chid2-ps1", "10:18").setVerified("-1"));
		commit2.addPatchSet(new PatchSet("chid2-ps2", "11:03"));
		commit2.addWorkItem(new WorkItem("JIRA-0002", ""));

		Commit commit3 = new Commit("chid3", "Bob", "user2", "team2", "project2", "master", "09:45");
		commit3.addPatchSet(new PatchSet("chid2-ps1", "09:45").setVerified("-1"));
		commit3.addPatchSet(new PatchSet("chid2-ps2", "09:48").setVerified("-1"));
		commit3.addPatchSet(new PatchSet("chid2-ps3", "09:50").setVerified("-1").setCodeReview("-2"));
		commit3.addPatchSet(new PatchSet("chid2-ps4", "09:52").setVerified("+1").setCodeReview("-2"));
		commit3.addPatchSet(new PatchSet("chid2-ps5", "09:53").setVerified("+1").setCodeReview("-2"));
		commit3.addPatchSet(new PatchSet("chid2-ps6", "09:56").setVerified("-1"));
		commit3.addPatchSet(new PatchSet("chid2-ps7", "10:00").setVerified("+1").setCodeReview("-1"));
		commit3.addPatchSet(new PatchSet("chid2-ps8", "10:01").setVerified("+1").setCodeReview("-1"));
		commit3.addPatchSet(new PatchSet("chid2-ps9", "10:02").setVerified("+1").setCodeReview("-1"));
		commit3.addPatchSet(new PatchSet("chid2-ps10", "10:03").setVerified("+1").setCodeReview("+2").setMerged("Yes"));
		commit3.addWorkItem(new WorkItem("JIRA-0003", ""));
		commit3.addWorkItem(new WorkItem("JIRA-0005", ""));
		
		commits.add(commit1);
		commits.add(commit2);
		commits.add(commit3);
		
		return commits;
	}

	public PersistenceLayer getPersistanceLayer() {
		return persistanceLayer;
	}
	
}
