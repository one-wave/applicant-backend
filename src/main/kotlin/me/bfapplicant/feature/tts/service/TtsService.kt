package me.bfapplicant.feature.tts.service

import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.google.cloud.texttospeech.v1.AudioConfig
import com.google.cloud.texttospeech.v1.AudioEncoding
import com.google.cloud.texttospeech.v1.SynthesisInput
import com.google.cloud.texttospeech.v1.TextToSpeechClient
import com.google.cloud.texttospeech.v1.VoiceSelectionParams
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class TtsService(
    private val ttsClient: TextToSpeechClient,
    private val storage: Storage,
    @Value("\${gcp.storage.bucket}") private val bucket: String
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @EventListener(ApplicationReadyEvent::class)
    fun ensureGuideAudioExists() {
        log.info("TTS guide check — verifying GCS audio files exist")
        val urls = generateAll(forceRegenerate = false)
        urls.forEach { (key, url) -> log.info("TTS guide ready — $key → $url") }
    }

    fun generateAll(forceRegenerate: Boolean): Map<String, String> =
        GUIDE_TEXTS.mapValues { (key, text) ->
            val objectPath = "$GCS_PREFIX/$key.mp3"
            if (!forceRegenerate && existsInGcs(objectPath)) {
                objectPath
            } else {
                uploadToGcs(objectPath, synthesize(text))
            }
        }

    private fun synthesize(text: String): ByteArray {
        val input = SynthesisInput.newBuilder().setText(text).build()

        val voice = VoiceSelectionParams.newBuilder()
            .setLanguageCode("ko-KR")
            .setName("ko-KR-Wavenet-A")
            .build()

        val audioConfig = AudioConfig.newBuilder()
            .setAudioEncoding(AudioEncoding.MP3)
            .setSpeakingRate(0.93)
            .build()

        return ttsClient.synthesizeSpeech(input, voice, audioConfig).audioContent.toByteArray()
    }

    private fun existsInGcs(objectPath: String): Boolean =
        storage.get(BlobId.of(bucket, objectPath)) != null

    fun readFromGcs(key: String): ByteArray? {
        val blob = storage.get(BlobId.of(bucket, "$GCS_PREFIX/$key.mp3")) ?: return null
        return blob.getContent()
    }

    private fun uploadToGcs(objectPath: String, audioBytes: ByteArray): String {
        val blobInfo = BlobInfo.newBuilder(BlobId.of(bucket, objectPath))
            .setContentType("audio/mpeg")
            .build()

        storage.create(blobInfo, audioBytes)
        return objectPath
    }

    companion object {
        private const val GCS_PREFIX = "tts-guides"

        val GUIDE_TEXTS = mapOf(
            "voice-resume-guide" to """
                안녕하세요! 이력서 작성을 도와드리겠습니다. 천천히 안내해드릴 테니 편하게 들어주세요.
                이 서비스는 녹음 버튼을 누르신 뒤 말씀하신 내용을 자동으로 이력서로 만들어드립니다.
                다섯 가지 항목이 있는데요, 해당하지 않는 항목은 편하게 건너뛰시면 됩니다.

                첫 번째는 학력입니다.
                학교 이름, 전공, 그리고 학위를 말씀해주세요.
                학위는 고등학교 졸업, 전문학사, 학사, 석사, 박사 중에서 해당하는 것을 말씀해주시면 됩니다.
                졸업 여부도 알려주세요. 졸업, 재학 중, 휴학 중, 중퇴, 졸업 예정 중에서 선택하시면 됩니다.
                입학 시기와 졸업 시기도 함께 말씀해주시면 더 좋습니다.
                예를 들어, 한밭대학교 기계공학과 학사, 2018년 3월 입학, 2022년 2월 졸업, 이런 식으로 말씀해주세요.
                고등학교까지만 졸업하셨다면, 대전고등학교 2016년 졸업, 이렇게만 말씀하셔도 충분합니다.

                두 번째는 경력입니다.
                회사 이름, 부서, 직위, 그리고 언제부터 언제까지 일하셨는지 말씀해주세요.
                지금도 다니고 계시다면 재직 중이라고 말씀해주시면 됩니다.
                어떤 일을 하셨는지 담당 업무도 간단히 설명해주시면 좋습니다.
                예를 들어, 행복마트 판매팀, 2020년 3월부터 2023년 6월까지, 매장 관리 및 고객 응대 담당, 이렇게 말씀해주세요.
                아르바이트나 단기 근무 경험도 모두 괜찮습니다.

                세 번째는 자격증입니다.
                자격증 이름, 발급 기관, 그리고 취득 시기를 말씀해주세요.
                예를 들어, 컴퓨터활용능력 2급, 대한상공회의소, 2021년 6월 취득, 이런 식으로 말씀해주시면 됩니다.
                운전면허나 자격증 종류에 상관없이 가지고 계신 것을 편하게 말씀해주세요.

                네 번째는 수상 경력입니다.
                수상명, 주최 기관, 수상 시기를 말씀해주시고, 어떤 내용으로 수상하셨는지도 간단히 알려주세요.
                예를 들어, 모범 사원상, 2022년 11월, 이런 식으로 말씀해주세요.

                다섯 번째는 외국어 능력입니다.
                어떤 언어인지, 시험 이름, 점수 또는 등급, 그리고 취득 시기를 말씀해주세요.
                예를 들어, 영어 토익 600점 2023년 3월 취득, 또는 한국어능력시험 5급, 이렇게 말씀해주시면 됩니다.
                점수가 높지 않더라도 괜찮으니 부담 없이 말씀해주세요.

                이제 준비가 되셨으면, 아래 녹음 버튼을 눌러주세요.
                잘 알아듣지 못해도 괜찮습니다. 녹음 후에 내용을 확인하고 수정하실 수 있으니 부담 갖지 마세요!
            """.trimIndent()
        )
    }
}
