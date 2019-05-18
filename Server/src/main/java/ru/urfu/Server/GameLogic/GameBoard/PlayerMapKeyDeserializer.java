package ru.urfu.Server.GameLogic.GameBoard;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.urfu.Server.GameLogic.GameObjects.IPlayer;
import ru.urfu.Server.GameLogic.GameObjects.Player;

import java.io.IOException;

public class PlayerMapKeyDeserializer extends KeyDeserializer {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Object deserializeKey(String s, DeserializationContext deserializationContext) throws IOException {
        return mapper.readValue(s, IPlayer.class);
    }
}
