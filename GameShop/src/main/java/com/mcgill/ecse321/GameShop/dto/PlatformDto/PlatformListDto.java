package com.mcgill.ecse321.GameShop.dto.PlatformDto;

import java.util.List;

public class PlatformListDto {
    private List<PlatformSummaryDto> platforms;
    public PlatformListDto(List<PlatformSummaryDto> platforms) {
        this.platforms = platforms;
    }
    public List<PlatformSummaryDto> getPlatforms() {
        return platforms;
    }
    public void setPlatforms(List<PlatformSummaryDto> platforms) {
        this.platforms = platforms;
    }
}
