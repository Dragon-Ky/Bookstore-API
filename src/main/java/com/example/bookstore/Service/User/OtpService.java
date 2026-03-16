package com.example.bookstore.Service.User;

import com.example.bookstore.Entity.AppUser;
import com.example.bookstore.Entity.ENUM.Type;
import com.example.bookstore.Entity.VerificationToken;
import com.example.bookstore.Repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final PasswordResetTokenRepository tokenRepository;
    public String createAndSaveOtp(AppUser user, Type type) {
        // 1. Tìm xem User này đã từng có token/otp nào trong DB chưa
        Optional<VerificationToken> existingToken = tokenRepository.findByUserId(user.getId());

        // 2. Tạo mã OTP mới (6 số)
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        VerificationToken token;

        if (existingToken.isPresent()) {
            // 3. NẾU ĐÃ CÓ: Lấy đối tượng cũ ra để ghi đè thông tin mới vào (Hibernate sẽ hiểu đây là UPDATE)
            token = existingToken.get();
        } else {
            // 4. NẾU CHƯA CÓ: Tạo đối tượng mới hoàn toàn (Hibernate sẽ hiểu đây là INSERT)
            token = new VerificationToken();
            token.setUser(user); // Chỉ cần set User một lần duy nhất khi tạo mới
        }

        // 5. Cập nhật các thông tin mới (áp dụng cho cả trường hợp tạo mới hoặc ghi đè)
        token.setOtp(otp);
        token.setType(type);
        token.setExpiryDate(LocalDateTime.now().plusMinutes(5));

        // 6. Lưu xuống DB (Nếu là token lấy từ DB lên, nó sẽ thực hiện lệnh SQL UPDATE thay vì INSERT)
        tokenRepository.save(token);

        return otp;
    }
}
