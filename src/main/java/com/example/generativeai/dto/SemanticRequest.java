package com.example.generativeai.dto;

import java.util.List;

public record SemanticRequest(List<String> sourceList, List<String> destinationList) {
}
