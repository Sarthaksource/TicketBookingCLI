package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainService {
    private List<Train> trainList;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TRAIN_DB_PATH = "D:\\JavaFullStack\\TicketBooking\\lib\\src\\main\\java\\ticket\\booking\\localDB\\trains.json";

    public TrainService() throws IOException {
        File trains = new File(TRAIN_DB_PATH);
        this.trainList = this.objectMapper.readValue(trains, new TypeReference<List<Train>>() {});
    }

    public List<Train> searchTrains(String source, String destination) {
        return this.trainList.stream().filter((train) -> this.validTrain(train, source, destination)).collect(Collectors.toList());
    }

    private boolean validTrain(Train train, String source, String destination) {
        List<String> stationOrder = train.getStations();
        int sourceIndex = stationOrder.indexOf(source.toLowerCase());
        int destinationIndex = stationOrder.indexOf(destination.toLowerCase());
        return sourceIndex != -1 && destinationIndex != -1 && sourceIndex < destinationIndex;
    }

    public void addTrain(Train newTrain) {
        Optional<Train> existingTrain = this.trainList.stream().filter((train) -> train.getTrainId().equalsIgnoreCase(newTrain.getTrainId())).findFirst();
        if (existingTrain.isPresent()) {
            this.updateTrain(newTrain);
        } else {
            this.trainList.add(newTrain);
            this.saveTrainListToFile();
        }
    }

    public void updateTrain(Train updatedTrain) {
        OptionalInt index = IntStream.range(0, this.trainList.size()).filter((i) -> ((Train)this.trainList.get(i)).getTrainId().equalsIgnoreCase(updatedTrain.getTrainId())).findFirst();
        if (index.isPresent()) {
            this.trainList.set(index.getAsInt(), updatedTrain);
            this.saveTrainListToFile();
        } else {
            this.addTrain(updatedTrain);
        }

    }

    private void saveTrainListToFile() {
        try {
            this.objectMapper.writeValue(new File(TRAIN_DB_PATH), this.trainList);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
