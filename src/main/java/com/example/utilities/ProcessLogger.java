package com.example.utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.example.model.GameEvent;

public class ProcessLogger {
    private static ProcessLogger instance;

    private final List<GameEvent> events = new ArrayList<>();
    private final String caseId;
    private ProcessLogger(String caseId) {this.caseId = caseId == null ? "GAME001" : caseId; }

    public static ProcessLogger getInstance(String caseId) {
        if (instance == null) instance = new ProcessLogger(caseId);
        return instance;
    }

    public String getCaseId() {
        return caseId;
    }

    public void log(GameEvent e){
        events.add(e); 
    }
    
    public void logSystem(String activity){ 
        log(new GameEvent(caseId,"System",activity,Instant.now(),"",null,"","",null)); 
    }

    public List<GameEvent> getEvents(){return events;}

    public void writeCsv(String outPath) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outPath))) {
            bw.write("Case_ID,Player_ID,Activity,Timestamp,Category,Question_Value,Answer_Given,Result,Score_After_Play");
            bw.newLine();

            DateTimeFormatter fmt = DateTimeFormatter.ISO_INSTANT;

            for (GameEvent e : events) {
                String ts = e.getTimestamp() == null ? "" : fmt.format(e.getTimestamp());
                String line = String.join(",",
                        safe(e.getCaseId()),
                        safe(e.getPlayerId()),
                        safe(e.getActivity()),
                        safe(ts),
                        safe(e.getCategory()),
                        e.getQuestionValue()==null?"":e.getQuestionValue().toString(),
                        safe(e.getAnswerGiven()),
                        safe(e.getResult()),
                        e.getScoreAfter()==null?"":e.getScoreAfter().toString()
                );
                bw.write(line);
                bw.newLine();
            }
        }
    }
    
    private String safe(String s){
        return s==null?"":("\"" + s.replace("\"","'")+"\""); 
    }
}