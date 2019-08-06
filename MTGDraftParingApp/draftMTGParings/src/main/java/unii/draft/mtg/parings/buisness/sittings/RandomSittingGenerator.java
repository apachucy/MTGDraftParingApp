package unii.draft.mtg.parings.buisness.sittings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RandomSittingGenerator implements ISittingGenerator {
    @NonNull
    @Override
    public List<String> generateSittings(@Nullable List<String> playerNameList) {
        if (playerNameList == null || playerNameList.isEmpty()) {
            return new ArrayList<>();
        }
        Random random = new Random();
        List<String> playerRandomOrderedNameList = new ArrayList<>();
        List<String> coppyPlayerNameList = new ArrayList<>(playerNameList);
        while (!coppyPlayerNameList.isEmpty()) {
            int randomNumber = random.nextInt(coppyPlayerNameList.size());
            playerRandomOrderedNameList.add(coppyPlayerNameList.get(randomNumber));
            coppyPlayerNameList.remove(randomNumber);
        }

        return playerRandomOrderedNameList;
    }
}
