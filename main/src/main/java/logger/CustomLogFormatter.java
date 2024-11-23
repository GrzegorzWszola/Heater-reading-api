package logger;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomLogFormatter extends Formatter {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String format(LogRecord record) {
        String timestamp = LocalDateTime.now().format(dateTimeFormatter);
        return String.format("[%s] %s: %s%n",
                timestamp,
                record.getLevel(),
                record.getMessage());
    }
}
