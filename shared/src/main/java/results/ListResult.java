package results;
import model.GameData;
import java.util.List;

public record ListResult(List<GameData> games) {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Games: \n");
        for(GameData game : games){
            sb.append(game.toString()).append("/n");
        }
        return sb.toString();
    }
}
